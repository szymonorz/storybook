package pl.szyorz.storybook.entity.user.data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UpdateUserRolesRequest(UUID userId, List<String> roles) {
}
