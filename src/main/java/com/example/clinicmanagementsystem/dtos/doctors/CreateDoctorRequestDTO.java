package com.example.clinicmanagementsystem.dtos.doctors;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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


    @Override
    public String toString() {
        return "CreateDoctorRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
