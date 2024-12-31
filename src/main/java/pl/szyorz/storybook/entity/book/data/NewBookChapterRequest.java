package pl.szyorz.storybook.entity.book.data;

import java.util.UUID;

public record NewBookChapterRequest(UUID bookId,
                                    String chapterTitle,
                                    String chapterDescription,
                                    String authorNote,
                                    String chapterContent) {
}
