package pl.szyorz.storybook.entity.user.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 3, max = 64) @Nullable String username,
        @Email @Nullable String email,
        @Size(min = 4, max = 128) @Nullable String password
) {}
