package pl.szyorz.storybook.entity.role;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szyorz.storybook.entity.role.data.RoleResponse;
import pl.szyorz.storybook.entity.user.data.DetailedUserResponse;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/role")
public class RoleController {
    private RoleService roleService;

    @PreAuthorize("hasAuthority('SUPERUSER') || hasAuthority('VIEW_ROLE')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleResponse>> allRoles() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }

    @PreAuthorize("hasAuthority('SUPERUSER')")
    @GetMapping("/privileges")
    public ResponseEntity<List<String>> listAllPrivileges() {
        return ResponseEntity.ok(roleService.listAllPrivileges());
    }

    @GetMapping("/{roleId}/users")
    public ResponseEntity<List<DetailedUserResponse>> usersByRole(
            @PathVariable UUID roleId
    ) {
        return ResponseEntity.ok(roleService.findUsersByRole(roleId));
    }

    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> assignRoleToUser(
            @PathVariable UUID roleId,
            @PathVariable UUID userId
    ) {
        roleService.assignRoleToUser(roleId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> removeRoleFromUser(
            @PathVariable UUID roleId,
            @PathVariable UUID userId
    ) {
        roleService.removeRoleFromUser(roleId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{roleId}/privileges")
    public ResponseEntity<RoleResponse> updatePrivileges(
            @PathVariable UUID roleId,
            @RequestBody List<String> privileges
    ) {
        return ResponseEntity.ok(roleService.updatePrivileges(roleId, privileges));
    }
}
