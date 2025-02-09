package pl.szyorz.storybook.entity.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DoesntExistException extends RuntimeException {
    private String message;
    public DoesntExistException(String message) {
        super(message);
        this.message = message;
    }
}
