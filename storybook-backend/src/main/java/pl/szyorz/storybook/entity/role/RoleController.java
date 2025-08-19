package pl.szyorz.storybook.entity.role;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szyorz.storybook.entity.role.data.RoleResponse;

import java.util.List;

@RestController
@AllArgsConstructor
public class RoleController {
    private RoleService roleService;

    @PreAuthorize("hasAuthority('SUPERUSER')")
    @GetMapping(value = "/api/role/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleResponse>> allRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
