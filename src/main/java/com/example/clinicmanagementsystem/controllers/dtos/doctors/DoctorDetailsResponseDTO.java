package com.example.clinicmanagementsystem.controllers.dtos.doctors;

import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;

import java.util.List;

public class DoctorDetailsResponseDTO {

    private String firstName;
    private String lastName;
    private String specialization;
    private String contactInfo;

    private List<AppointmentResponseDTO> appointments;

    public List<AppointmentResponseDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentResponseDTO> appointments) {
        this.appointments = appointments;
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
