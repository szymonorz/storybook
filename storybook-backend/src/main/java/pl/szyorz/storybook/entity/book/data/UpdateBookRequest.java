package pl.szyorz.storybook.entity.book.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record UpdateBookRequest(
        @Size(min = 1, max = 200) @Nullable String title,
        @Size(min = 0, max = 2000) @Nullable String description
) {}
