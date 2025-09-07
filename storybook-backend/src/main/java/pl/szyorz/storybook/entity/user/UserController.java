package pl.szyorz.storybook.entity.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szyorz.storybook.entity.user.data.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/register")
    public ResponseEntity<UserCreatedResponse> registerUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        return userService.createNewUser(request)
                .map(user -> ResponseEntity.ok(new UserCreatedResponse(user.getId())))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/api/currentuser")
    public ResponseEntity<DetailedUserResponse> currentUser(
            Principal principal
    ) {
        return userService.findByUsername(principal.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

//    @PutMapping("/api/user/roles")
//    public ResponseEntity<String>

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<UserWithoutRolesResponse> getUser(
            @PathVariable("userId") UUID userId
    ) {
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PatchMapping("/api/user/{userId}")
    public ResponseEntity<UserWithoutRolesResponse> updateUser(
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody UpdateUserRequest request,
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return userService.updateUser(userId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).build());
    }

}
