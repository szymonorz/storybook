package pl.szyorz.storybook.entity.chapter.data;

public record UpdateChapterRequest(String title,
                                   String description,
                                   String authorsNote,
                                   String content) {
}
