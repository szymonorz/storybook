package pl.szyorz.storybook.entity.chapter.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record UpdateChapterRequest(
        @Size(min = 1, max = 2000) @Nullable String chapterTitle,
        @Size(min = 0, max = 2000) @Nullable String chapterDescription,
        @Size(min = 0, max = 2000) @Nullable String authorsNote,
        @Size(min = 5, max = 10_000) @Nullable String chapterContent) {
}
