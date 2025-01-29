package pl.szyorz.storybook.entity.chapter.data;

import java.util.UUID;

public record ShortChapterResponse(UUID id, String title, String description, int position) {
}
