package pl.szyorz.storybook.entity.user.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Valid
public record CreateUserRequest(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password) {
}
