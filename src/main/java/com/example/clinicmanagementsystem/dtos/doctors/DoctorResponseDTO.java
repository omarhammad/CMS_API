package com.example.clinicmanagementsystem.dtos.doctors;

import com.example.clinicmanagementsystem.domain.Appointment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DoctorResponseDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String contactInfo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }


}
