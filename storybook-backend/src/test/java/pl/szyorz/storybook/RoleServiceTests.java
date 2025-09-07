package pl.szyorz.storybook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.RoleRepository;
import pl.szyorz.storybook.entity.role.RoleService;
import pl.szyorz.storybook.entity.role.data.CreateRoleRequest;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    private Role roleAdmin;
    private Role roleEditor;
    private UUID roleId;
    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();
        userId = UUID.randomUUID();

        roleAdmin = new Role();
        roleAdmin.setId(roleId);
        roleAdmin.setName("ADMIN");
        roleAdmin.setDescription("admin role");
        roleAdmin.setPrivileges(new ArrayList<>(List.of(RolePrivilege.SUPERUSER)));

        roleEditor = new Role();
        roleEditor.setId(UUID.randomUUID());
        roleEditor.setName("MOD");
        roleEditor.setDescription("editor role");
        roleEditor.setPrivileges(new ArrayList<>(List.of(RolePrivilege.MODERATE_CONTENT, RolePrivilege.MODERATE_USERS)));

        user = new User();
        user.setId(userId);
        user.setUsername("adam");
        user.setEmail("a@b.com");
        user.setRoles(new ArrayList<>());
    }

    // -------------------- createRole --------------------

    @Test
    void createRole_savesAndReturnsRole() {
        CreateRoleRequest req = new CreateRoleRequest(
                "ROLE_MANAGER",
                "manages roles",
                List.of(RolePrivilege.MODIFY_ROLE, RolePrivilege.CREATE_ROLE, RolePrivilege.DELETE_ROLE, RolePrivilege.VIEW_ROLE)
        );

        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> {
            Role r = inv.getArgument(0);
            r.setId(UUID.randomUUID());
            return r;
        });

        var out = roleService.createRole(req);

        assertTrue(out.isPresent());
        Role saved = out.get();
        assertEquals("ROLE_MANAGER", saved.getName());
        assertEquals("manages roles", saved.getDescription());
        assertEquals(List.of(
                        RolePrivilege.MODIFY_ROLE,
                        RolePrivilege.CREATE_ROLE,
                        RolePrivilege.DELETE_ROLE,
                        RolePrivilege.VIEW_ROLE), saved.getPrivileges());

        verify(roleRepository).save(any(Role.class));
    }


    @Test
    void listAllPrivileges_returnsEnumNames() {
        var names = roleService.listAllPrivileges();
        Arrays.stream(RolePrivilege.values()).iterator().forEachRemaining(
                r -> assertTrue(names.contains(r.name()))
        );
    }


    @Test
    void getRolesByNames_delegatesToRepository() {
        when(roleRepository.findByNameIn(List.of("ADMIN", "MOD"))).thenReturn(List.of(roleAdmin, roleEditor));

        var out = roleService.getRolesByNames(List.of("ADMIN", "MOD"));

        assertEquals(2, out.size());
        assertEquals("ADMIN", out.get(0).getName());
        assertEquals("MOD", out.get(1).getName());
        verify(roleRepository).findByNameIn(List.of("ADMIN", "MOD"));
    }


    @Test
    void getUserRoles_delegatesToRepository() {
        when(roleRepository.findAllByUsersId(userId)).thenReturn(List.of(roleAdmin));

        var out = roleService.findRolesByUserId(userId);

        assertEquals(1, out.size());
        assertEquals("ADMIN", out.get(0).getName());
        verify(roleRepository).findAllByUsersId(userId);
    }


    @Test
    void getAllRoles_mapsEntitiesToRoleResponse() {
        when(roleRepository.findAll()).thenReturn(List.of(roleAdmin, roleEditor));

        var out = roleService.findAllRoles();

        assertEquals(2, out.size());
        RoleResponse r0 = out.get(0);
        assertEquals(roleAdmin.getId(), r0.id());
        assertEquals("ADMIN", r0.name());
        assertEquals("admin role", r0.description());
        assertEquals(roleAdmin.getPrivileges(), r0.privileges());
    }


    @Test
    void findUsersByRole_mapsUsersViaConverter() {

        user.setRoles(List.of(roleAdmin));
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("eve");
        user2.setEmail("e@x.com");
        user2.setRoles(List.of(roleEditor));

        when(userRepository.findAllByRolesId(roleId)).thenReturn(List.of(user, user2));

        var out = roleService.findUsersByRole(roleId);

        assertEquals(2, out.size());
        assertEquals(user.getId(), out.get(0).id());
        assertEquals(user.getUsername(), out.get(0).username());
        assertEquals(user.getEmail(), out.get(0).email());
        assertFalse(out.get(0).userRoles().isEmpty());

        assertEquals("eve", out.get(1).username());
    }


    @Test
    void assignRoleToUser_addsRoleAndSavesUser() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleAdmin));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        roleService.assignRoleToUser(roleId, userId);

        assertEquals(1, user.getRoles().size());
        assertEquals("ADMIN", user.getRoles().get(0).getName());
        verify(userRepository).save(user);
    }

    @Test
    void assignRoleToUser_throwsWhenRoleMissing() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.assignRoleToUser(roleId, userId));
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void assignRoleToUser_throwsWhenUserMissing() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleAdmin));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.assignRoleToUser(roleId, userId));
        verify(userRepository, never()).save(any());
    }

    // -------------------- removeRoleFromUser --------------------

    @Test
    void removeRoleFromUser_removesMatchingRoleAndSaves() {
        Role another = new Role();
        another.setId(UUID.randomUUID());
        another.setName("OTHER");

        user.setRoles(new ArrayList<>(List.of(roleAdmin, another)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        roleService.removeRoleFromUser(roleId, userId);

        assertEquals(1, user.getRoles().size());
        assertEquals("OTHER", user.getRoles().get(0).getName());
        verify(userRepository).save(user);
    }

    @Test
    void removeRoleFromUser_throwsWhenUserMissing() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.removeRoleFromUser(roleId, userId));
        verify(userRepository, never()).save(any());
    }

    // -------------------- updatePrivileges --------------------

    @Test
    void updatePrivileges_setsPrivileges_andReturnsMappedResponse() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleAdmin));
        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        List<String> privs = List.of("VIEW_ROLE", "CREATE_ROLE");
        RoleResponse resp = roleService.updatePrivileges(roleId, privs);

        assertEquals(roleId, resp.id());
        assertEquals("ADMIN", resp.name());
        assertTrue(resp.privileges().contains(RolePrivilege.VIEW_ROLE));
        assertTrue(resp.privileges().contains(RolePrivilege.CREATE_ROLE));
        assertEquals(roleAdmin.getPrivileges(), resp.privileges());

        verify(roleRepository).save(roleAdmin);
    }

    @Test
    void updatePrivileges_throwsOnInvalidPrivilegeString() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleAdmin));

        assertThrows(IllegalArgumentException.class,
                () -> roleService.updatePrivileges(roleId, List.of("NOT_A_PRIV")));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void updatePrivileges_throwsWhenRoleMissing() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.updatePrivileges(roleId, List.of("VIEW_ROLE")));
        verify(roleRepository, never()).save(any());
    }
}
