package com.example.clinicmanagementsystem.controllers.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.context.annotation.Conditional;

public class RegisterViewModel {

    @NotBlank(message = "Username must be provided!")
    private String username;

    @NotBlank(message = "password must be provided!")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters!")
    private String password;
    @NotBlank(message = "password must be provided!")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters!")
    private String confirmPassword;


    public RegisterViewModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "RegisterViewModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
