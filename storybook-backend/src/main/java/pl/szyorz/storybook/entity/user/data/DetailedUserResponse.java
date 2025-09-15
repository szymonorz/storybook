package pl.szyorz.storybook.entity.user.data;

import java.util.UUID;

public record DetailedUserResponse(UUID id, String username, String email) {
}
