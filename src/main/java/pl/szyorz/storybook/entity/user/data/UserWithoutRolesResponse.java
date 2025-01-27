package pl.szyorz.storybook.entity.user.data;

import java.util.UUID;

public record UserWithoutRolesResponse(UUID userId, String username) {
}
