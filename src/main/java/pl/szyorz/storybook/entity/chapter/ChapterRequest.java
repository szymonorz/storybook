package pl.szyorz.storybook.entity.chapter;

import java.util.UUID;


public record ChapterRequest(String name,
                             String description,
                             String content,
                             UUID previousChapter,
                             UUID nextChapter) {
}