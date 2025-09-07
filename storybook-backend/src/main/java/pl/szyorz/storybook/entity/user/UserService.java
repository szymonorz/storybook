package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.RoleService;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.user.data.*;
import pl.szyorz.storybook.entity.user.exception.AlreadyExistsException;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleService roleService;

    public Optional<User> createNewUser(CreateUserRequest req) {
        if(userRepository.existsByEmail(req.email())) {
            throw new AlreadyExistsException("Account with this username already exists");
        }

        if(userRepository.existsByUsername(req.username())) {
            throw new AlreadyExistsException("Account with this username already exists");
        }

        User user = new User();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));

        User saved = userRepository.save(user);
        return Optional.of(saved);
    }

    public void updateUsersRoles(UpdateUserRolesRequest req) {
        Optional<User> userOptional = userRepository.findById(req.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("No such user");
        }
        User user = userOptional.get();

        List<Role> rolesInDB = roleService.getRolesByNames(req.roles());
        user.setRoles(rolesInDB);
        userRepository.save(user);
    }

    public Optional<UserWithoutRolesResponse> findById(UUID userId) {
        return userRepository.findById(userId).map(user -> new UserWithoutRolesResponse(user.getId(), user.getUsername()));
    }

    public List<Role> findUserRoles(UUID userId) {
        return roleService.findRolesByUserId(userId);
    }

    @PreAuthorize("hasAuthority('SUPERUSER') or hasAuthority('MODERATE_USERS') or @userSecurity.isSelf(#userId, authentication)")
    public Optional<UserWithoutRolesResponse> updateUser(UUID userId, UpdateUserRequest req) {
        var ou = userRepository.findById(userId);
        if (ou.isEmpty()) return Optional.empty();
        var user = ou.get();

        if (req.username() != null && !req.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(req.username())) {
                throw new AlreadyExistsException("username already exists");
            }
            user.setUsername(req.username());
        }

        if (req.email() != null && !req.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.email())) {
                throw new AlreadyExistsException("email already exists");
            }
            user.setEmail(req.email());
        }

        if (req.password() != null) {
            user.setPassword(passwordEncoder.encode(req.password()));
        }

        var saved = userRepository.save(user);
        return Optional.of(new UserWithoutRolesResponse(saved.getId(), saved.getUsername()));
    }

    public Optional<DetailedUserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserService::convertDBUserToAPIUser);
    }

    public static DetailedUserResponse convertDBUserToAPIUser(User user) {
        return new DetailedUserResponse(user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getRoles()
                                        .stream()
                                        .map(role ->  new RoleResponse(
                                                role.getId(),
                                                role.getName(),
                                                role.getDescription(),
                                                role.getPrivileges()
                                        )).toList()
        );
    }


}
