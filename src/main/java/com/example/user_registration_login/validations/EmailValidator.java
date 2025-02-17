package com.example.user_registration_login.validations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.EmailValidation.class)
@Documented
public @interface EmailValidator {
    String message() default "Invalid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class EmailValidation implements ConstraintValidator<EmailValidator, String> {
        private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        private Pattern pattern;

        @Override
        public void initialize(EmailValidator constraintAnnotation) {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (email == null || email.isEmpty()) {
                return false;
            }
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }
}
