package pl.szyorz.storybook.entity.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Password must be at least 8 characters, contain upper, lower, digit, and one of !@#$&*";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
