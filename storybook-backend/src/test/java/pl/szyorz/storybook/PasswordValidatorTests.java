package pl.szyorz.storybook;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import pl.szyorz.storybook.entity.user.validator.PasswordValidator;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class PasswordValidatorTests {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void shouldReturnFalseOnInvalidPassword() {
        String badPassword = "abc123";
        assertFalse(validator.isValid(badPassword, null));
    }

    @Test
    void shouldReturnTrue() {
        assertTrue(validator.isValid(null, null));
    }
}
