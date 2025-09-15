package pl.szyorz.storybook.entity.book.data;

import pl.szyorz.storybook.entity.chapter.data.ShortChapterResponse;
import pl.szyorz.storybook.entity.user.data.UserResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookResponse (UUID id, String title, String description, UserResponse author, LocalDateTime createdAt, LocalDateTime updatedAt, List<ShortChapterResponse> chapters){
}
