package pl.szyorz.storybook.entity.chapter.data;

import java.util.UUID;

public record ChapterContentResponse(UUID id, String title, String description, String authorsNote, String content, int position) {
}
