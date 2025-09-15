package pl.szyorz.storybook.entity.user.data;

import java.util.UUID;

public record UserResponse(UUID id, String username) {
}
