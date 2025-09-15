package pl.szyorz.storybook.entity.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.szyorz.storybook.entity.common.ErrorResponse;

@ControllerAdvice
public class BookControllerExceptionHandler {
        @ExceptionHandler(value = BookNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public @ResponseBody ErrorResponse handleBookNotFoundException(BookNotFoundException ex) {
            return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        }
}
