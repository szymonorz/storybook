package pl.szyorz.storybook.entity.user.data;

import pl.szyorz.storybook.entity.role.Role;

import java.util.List;

public record UserResponse(String username, String email, List<Role> userRoles) {
}
