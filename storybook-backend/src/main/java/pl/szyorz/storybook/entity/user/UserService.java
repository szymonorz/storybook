package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.user.data.*;
import pl.szyorz.storybook.entity.user.exception.AlreadyExistsException;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

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

    public Optional<UserWithoutRolesResponse> findById(UUID userId) {
        return userRepository.findById(userId).map(user -> new UserWithoutRolesResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @PreAuthorize("@userSecurity.isSelf(#userId, authentication)")
    public Optional<UserWithoutRolesResponse> updateUser(UUID userId, UpdateUserRequest req) {
        var ou = userRepository.findById(userId);
        if (ou.isEmpty()) return Optional.empty();
        var user = ou.get();

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
        return Optional.of(new UserWithoutRolesResponse(saved.getId(), saved.getUsername(), saved.getEmail()));
    }

    public Optional<DetailedUserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserService::convertDBUserToAPIUser);
    }

    public static DetailedUserResponse convertDBUserToAPIUser(User user) {
        return new DetailedUserResponse(user.getId(),
                                user.getUsername(),
                                user.getEmail()
        );
    }


}
