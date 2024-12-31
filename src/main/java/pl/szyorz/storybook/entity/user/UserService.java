package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.user.data.CreateUserRequest;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

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

        return userRepository.save(user);
    }
}
