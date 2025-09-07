package pl.szyorz.storybook.entity.book.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @Size(min = 1, max = 200) @NotNull String title,
        @Size(min = 1, max = 2000) @Nullable String description) {
}
