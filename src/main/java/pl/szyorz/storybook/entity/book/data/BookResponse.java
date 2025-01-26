package pl.szyorz.storybook.entity.book.data;

import pl.szyorz.storybook.entity.chapter.data.ChapterResponse;
import pl.szyorz.storybook.entity.user.data.UserResponse;

import java.util.List;
import java.util.UUID;

public record BookResponse (UUID id, String title, String description, UserResponse author, List<ChapterResponse> chapters){
}
