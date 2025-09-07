package pl.szyorz.storybook.entity.book.data;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewBookChapterRequest(
        @NotNull UUID bookId,
        @Size(min = 0, max = 200) @NotNull String chapterTitle,
        @Size(min = 0, max = 2000 ) @Nullable String chapterDescription,
        @Size(min = 0, max = 2000) @Nullable String authorNote,
        @Size(min = 5, max = 10_000) @NotNull  String chapterContent) {
}
