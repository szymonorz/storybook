package pl.szyorz.storybook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.RoleService;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;
import pl.szyorz.storybook.entity.user.UserService;
import pl.szyorz.storybook.entity.user.data.*;
import pl.szyorz.storybook.entity.user.exception.AlreadyExistsException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserRepository userRepository;
    @Mock private RoleService roleService;

    @InjectMocks private UserService userService;

    private User existing;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        existing = new User();
        existing.setId(userId);
        existing.setUsername("adam");
        existing.setEmail("a@b.com");
        existing.setPassword("ENC");
    }

    @Test
    void createNewUser_success_encodesPassword_andSaves() {
        CreateUserRequest req = new CreateUserRequest("eve", "e@x.com", "pwd");
        when(userRepository.existsByEmail("e@x.com")).thenReturn(false);
        when(userRepository.existsByUsername("eve")).thenReturn(false);
        when(passwordEncoder.encode("pwd")).thenReturn("ENC(pwd)");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        Optional<User> out = userService.createNewUser(req);
        assertTrue(out.isPresent());
        assertEquals("eve", out.get().getUsername());
        assertEquals("e@x.com", out.get().getEmail());
        assertEquals("ENC(pwd)", out.get().getPassword());

        verify(passwordEncoder).encode("pwd");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createNewUser_throwsWhenEmailExists() {
        CreateUserRequest req = new CreateUserRequest("eve", "e@x.com", "pwd");
        when(userRepository.existsByEmail("e@x.com")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.createNewUser(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createNewUser_throwsWhenUsernameExists() {
        CreateUserRequest req = new CreateUserRequest("eve", "e@x.com", "pwd");
        when(userRepository.existsByEmail("e@x.com")).thenReturn(false);
        when(userRepository.existsByUsername("eve")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.createNewUser(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUsersRoles_setsRolesFromService_andSaves() {
        UpdateUserRolesRequest req = new UpdateUserRolesRequest(userId, List.of("ADMIN", "EDITOR"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));

        Role r1 = new Role(); r1.setName("ADMIN");
        Role r2 = new Role(); r2.setName("EDITOR");
        when(roleService.getRolesByNames(List.of("ADMIN", "EDITOR"))).thenReturn(List.of(r1, r2));

        userService.updateUsersRoles(req);

        assertEquals(2, existing.getRoles().size());
        assertEquals("ADMIN", existing.getRoles().get(0).getName());
        verify(userRepository).save(existing);
    }

    @Test
    void updateUsersRoles_throwsWhenUserMissing() {
        UpdateUserRolesRequest req = new UpdateUserRolesRequest(UUID.randomUUID(), List.of("ADMIN"));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUsersRoles(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUser_mapsToUserWithoutRolesResponse() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        Optional<UserWithoutRolesResponse> out = userService.findById(userId);
        assertTrue(out.isPresent());
        assertEquals(userId, out.get().userId());
        assertEquals("adam", out.get().username());
    }

    @Test
    void updateUser_updatesProvidedFields_checksUniqueness_andEncodesPassword() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsername("adam2")).thenReturn(false);
        when(userRepository.existsByEmail("new@mail.tld")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("ENC(1234)");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserRequest req = new UpdateUserRequest("adam2", "new@mail.tld", "1234");
        Optional<UserWithoutRolesResponse> out = userService.updateUser(userId, req);

        assertTrue(out.isPresent());
        assertEquals("adam2", out.get().username());
        assertEquals("ENC(1234)", existing.getPassword());
        verify(passwordEncoder).encode("1234");
    }

    @Test
    void updateUser_throwsWhenUsernameTaken() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        UpdateUserRequest req = new UpdateUserRequest("taken", null, null);
        assertThrows(AlreadyExistsException.class, () -> userService.updateUser(userId, req));
    }

    @Test
    void updateUser_throwsWhenEmailTaken() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("new@mail.tld")).thenReturn(true);

        UpdateUserRequest req = new UpdateUserRequest(null, "new@mail.tld", null);
        assertThrows(AlreadyExistsException.class, () -> userService.updateUser(userId, req));
    }

    @Test
    void updateUser_returnsEmptyWhenUserMissing() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UpdateUserRequest req = new UpdateUserRequest("x", null, null);
        assertTrue(userService.updateUser(userId, req).isEmpty());
        verify(userRepository, never()).save(any());
    }
}
