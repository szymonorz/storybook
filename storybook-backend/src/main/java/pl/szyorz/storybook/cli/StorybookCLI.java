package pl.szyorz.storybook.cli;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserService;
import pl.szyorz.storybook.entity.user.data.CreateUserRequest;

import java.util.Optional;
import java.util.List;


@ShellComponent
@AllArgsConstructor
public class StorybookCLI {
    private final UserService userService;
    @ShellMethod(key = {"admin create"}, value = "Create admin user")
    public String createAdmin() {
        CreateUserRequest request = new CreateUserRequest(
                "admin",
                "admin",
                "admin"
        );
        Optional<User> userOptional = userService.createNewUser(request);
        if (userOptional.isEmpty()) {
            return "Failed to create admin user";
        }
        User user = userOptional.get();

        return "Created user admin:admin";
    }
}
