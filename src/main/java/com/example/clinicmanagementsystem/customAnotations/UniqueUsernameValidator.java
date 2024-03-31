package com.example.clinicmanagementsystem.customAnotations;


import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private IStakeholderService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true; // null case to be handled by @NotNull
        }
        return userService.getStakeholderByUsername(username) == null;
    }
}
