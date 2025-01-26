package pl.szyorz.storybook.entity.user.data;

import pl.szyorz.storybook.entity.role.data.RoleResponse;

import java.util.List;
import java.util.UUID;

public record UserResponse(UUID id, String username, String email, List<RoleResponse> userRoles) {
}
