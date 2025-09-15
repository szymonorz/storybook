package pl.szyorz.storybook;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import pl.szyorz.storybook.entity.user.data.CreateUserRequest;
import pl.szyorz.storybook.entity.user.data.UpdateUserRequest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        factory.close();
    }

    @Test
    void createUser_shouldPass() {
        CreateUserRequest req = new CreateUserRequest(
                "adam",
                "adam@example.com",
                "Abcdef1!"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(req);

        assertTrue(violations.isEmpty(), "Expected no violations for valid input");
    }

    @Test
    void createUser_shouldFailOnWeakPassword() {
        CreateUserRequest req = new CreateUserRequest(
                "adam",
                "adam@example.com",
                "weak"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Expected violation for weak password");

        String message = violations.iterator().next().getMessage();
        assertTrue(message.contains("Password must be at least"), "Got message: " + message);
    }

    @Test
    void createUser_shouldFailOnNullPassword() {
        CreateUserRequest req = new CreateUserRequest(
                "adam",
                "adam@example.com",
                null
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty(), "Expected violation because password is null");
    }

    @Test
    void updateUser_shouldPass() {
        UpdateUserRequest req = new UpdateUserRequest(
                "adam@example.com", "Abcdef1!"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty(), "Expected no violations for valid input");
    }

    @Test
    void updateUser_shouldPassOnEitherBeingNull() {
        UpdateUserRequest req = new UpdateUserRequest(
                "adam@example.com", null
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty(), "Expected no violations for valid input");
        UpdateUserRequest req2 = new UpdateUserRequest(
                null, "Abcdef1!"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations2 = validator.validate(req2);
        assertTrue(violations2.isEmpty(), "Expected no violations for valid input");
    }

    @Test
    void updateUser_shouldFailOnInvalidEmail() {
        UpdateUserRequest req = new UpdateUserRequest(
                "adam#example.com", "Abcdef1!"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Expected violation because email is invalid");
    }

    @Test
    void updateUser_shouldFailOnWeakPassword() {
        UpdateUserRequest req = new UpdateUserRequest(
                "adam@example.com", "xxx"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Expected violation because password is weak");
    }
}

