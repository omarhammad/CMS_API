package com.example.clinicmanagementsystem.dtos.auth;

import com.example.clinicmanagementsystem.customAnotations.PasswordsMatch;
import com.example.clinicmanagementsystem.customAnotations.UniqueUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordsMatch(password = "password", confirmPassword = "confirmPassword", message = "Passwords do not match")

public class SignUpRequestDTO {


    @NotBlank(message = "Username must be provided!")
    @UniqueUsername(message = "Username already exists!")
    private String username;


    @NotBlank(message = "First Name must be provided!")
    private String firstName;
    @NotBlank(message = "Last Name must be provided!")
    private String lastName;
    private String gender;

    @NotBlank(message = "National Number must be provided!")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "National Number must be provided e.g 'yy.mm.dd-xxx.cd' ")
    private String nationalNumber;


    @NotBlank(message = "password must be provided!")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters!")
    private String password;
    @NotBlank(message = "password must be provided!")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters!")
    private String confirmPassword;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
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
        return "SignUpRequestDTO{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", nationalNumber='" + nationalNumber + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
