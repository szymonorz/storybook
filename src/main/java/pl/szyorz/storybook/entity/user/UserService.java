package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.RoleService;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.user.data.*;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleService roleService;

    public Optional<User> createNewUser(CreateUserRequest req) {
        if(userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Account with this email already exists");
        }

        if(userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Account with this username already exists");
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
        user.setUserRoles(rolesInDB);
        userRepository.save(user);
    }

    public Optional<UserWithoutRolesResponse> getUser(UUID userId) {
        return userRepository.findById(userId).map(user -> new UserWithoutRolesResponse(user.getId(), user.getUsername()));
    }

    public List<Role> getUserRoles(UUID userId) {
        return roleService.getUserRoles(userId);
    }

    /*
        Check if login request is valid.
     */
    public Optional<UserResponse> verifyUser(LoginRequest req) {
        Optional<User> userOptional = userRepository.findByEmail(req.email());
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        if(!passwordEncoder.matches(req.password(), user.getPassword())){
            return Optional.empty();
        }
        UserResponse resp = convertDBUserToAPIUser(user);
        return Optional.of(resp);
    }

    public Optional<UserResponse> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertDBUserToAPIUser);
    }

    private UserResponse convertDBUserToAPIUser(User user) {
        return new UserResponse(user.getId(),
                                user.getUsername()
        );
    }


}
