package pl.szyorz.storybook.entity.chapter.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChapterContentResponse(UUID id,
                                     String title,
                                     String description,
                                     String authorsNote,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt,
                                     String content,
                                     int position) {
}
