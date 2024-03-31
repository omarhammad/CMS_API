package com.example.clinicmanagementsystem.customAnotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    private String passwordFieldName;
    private String confirmPasswordFieldName;

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.password();
        this.confirmPasswordFieldName = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object password = new BeanWrapperImpl(value).getPropertyValue(passwordFieldName);
        Object confirmPassword = new BeanWrapperImpl(value).getPropertyValue(confirmPasswordFieldName);

        if (password == null || confirmPassword == null) {
            return false; // Consider valid if the fields are null to let @NotNull handle it
        }

        boolean valid = password.equals(confirmPassword);
        if (!valid) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(confirmPasswordFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
