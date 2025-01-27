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

import java.security.Principal;
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

    @GetMapping("/api/currentuser")
    public ResponseEntity<UserResponse> currentUser(Principal principal) {
        return userService.getByUsername(principal.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
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
