package pl.szyorz.storybook.entity.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyExistsException extends RuntimeException{
    private String message;

    public AlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
