package pl.szyorz.storybook.entity.book.data;

import java.util.UUID;

public record ChapterOrderUpdateRequest(UUID bookId,
                                        UUID chapterId,
                                        int desiredPosition) {
}
