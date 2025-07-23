package pl.szyorz.storybook.cli;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import pl.szyorz.storybook.entity.role.RoleService;
import pl.szyorz.storybook.entity.role.data.CreateRoleRequest;
import pl.szyorz.storybook.entity.role.privilege.RolePrivilege;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserService;
import pl.szyorz.storybook.entity.user.data.CreateUserRequest;
import pl.szyorz.storybook.entity.user.data.UpdateUserRolesRequest;

import java.util.Optional;
import java.util.List;


@ShellComponent
@AllArgsConstructor
public class StorybookCLI {
    private final UserService userService;
    private final RoleService roleService;

    @ShellMethod(key = {"admin create"}, value = "Create admin user")
    public String createAdmin() {
        // Create admin role
        CreateRoleRequest roleRequest = new CreateRoleRequest(
                "admin",
                "Admin role",
                List.of(RolePrivilege.SUPERUSER)
        );

        roleService.createRole(roleRequest);


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

        UpdateUserRolesRequest updateUserRolesRequest = new UpdateUserRolesRequest(
                user.getId(),
                List.of("admin")
        );

        userService.updateUsersRoles(updateUserRolesRequest);


        return "Created user admin:admin";
    }
}
