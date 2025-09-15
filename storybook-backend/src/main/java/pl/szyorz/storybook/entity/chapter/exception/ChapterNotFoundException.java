package pl.szyorz.storybook.entity.chapter.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChapterNotFoundException extends RuntimeException {
    private String message;
    public ChapterNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}

