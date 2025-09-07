package pl.szyorz.storybook.entity.chapter.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record UpdateChapterRequest(
        @Size(min = 1, max = 2000) @Nullable String title,
        @Size(min = 0, max = 2000) @Nullable String description,
        @Size(min = 0, max = 2000) @Nullable String authorsNote,
        @Size(min = 5, max = 10_000) @Nullable String content) {
}
