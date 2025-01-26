package pl.szyorz.storybook.entity.chapter.data;

import java.util.UUID;

public record ChapterResponse(UUID id, String title, String description, int position) {
}
