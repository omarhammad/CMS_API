package com.example.clinicmanagementsystem.dtos.doctors;

import com.example.clinicmanagementsystem.customAnotations.PasswordsMatch;
import com.example.clinicmanagementsystem.customAnotations.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordsMatch(password = "password", confirmPassword = "confirmPassword", message = "Passwords do not match")
public class CreateDoctorRequestDTO {

    @NotBlank(message = "First name must be provided!")
    private String firstName;
    @NotBlank(message = "Last name must be provided!")
    private String lastName;
    @NotBlank(message = "Specialization must be provided!")
    private String specialization;
    @NotBlank(message = "Phone Number must be provided!")
    @Pattern(regexp = "(^\\+|00)\\d{1,3}[\\s-]?\\d{1,4}[\\s-]?\\d{1,4}[\\s-]?\\d{1,4}$", message = "Phone number format should be '+32xxx xxxxxx' ")
    private String phoneNumber;

    @NotBlank(message = "Email must be provided!")
    @Email(message = "Email format should be 'example@email.com' ")
    private String email;

    @NotBlank(message = "Username must be provided!")
    @UniqueUsername(message = "Username already exists!")
    private String username;

    @NotBlank(message = "password must be provided!")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters!")
    private String password;


    @NotBlank(message = "password must be provided!")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters!")
    private String confirmPassword;

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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
