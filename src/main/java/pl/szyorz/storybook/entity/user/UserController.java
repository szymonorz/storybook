package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.user.data.CreateUserRequest;
import pl.szyorz.storybook.entity.user.data.UserResponse;
import pl.szyorz.storybook.entity.user.data.UserWithoutRolesResponse;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user")
    public ResponseEntity<UUID> registerUser(@RequestBody CreateUserRequest request) {
        return userService.createNewUser(request)
                .map(user -> ResponseEntity.ok(user.getId()))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/api/user")
    public ResponseEntity<UserResponse> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Role> userRoles = userService.getUserRoles(user.getId());
        return ResponseEntity.ok(
                new UserResponse(user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        userRoles.stream().map(role -> new RoleResponse(
                                role.getId(),
                                role.getName(),
                                role.getDescription(),
                                role.getPrivileges()
                        )).toList()));
    }

//    @PutMapping("/api/user/roles")
//    public ResponseEntity<String>

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<UserWithoutRolesResponse> getUser(@PathVariable("userId") UUID userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

}
