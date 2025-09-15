package pl.szyorz.storybook.entity.user.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.szyorz.storybook.entity.user.validator.StrongPassword;

@Valid
public record CreateUserRequest(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String email,
        @StrongPassword @NotNull @NotBlank String password) {
}
