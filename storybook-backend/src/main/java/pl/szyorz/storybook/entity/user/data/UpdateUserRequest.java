package pl.szyorz.storybook.entity.user.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import pl.szyorz.storybook.entity.user.validator.StrongPassword;

public record UpdateUserRequest(
        @Email @Nullable String email,
        @Size(min = 8, max = 128) @StrongPassword @Nullable String password
) {}
