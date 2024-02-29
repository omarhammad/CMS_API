package com.example.clinicmanagementsystem.dtos.doctors;

import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.dtos.patients.PatientResponseDTO;

import java.util.List;

public class DoctorDetailsResponseDTO {

    private String firstName;
    private String lastName;
    private String specialization;
    private String contactInfo;

    public List<PatientResponseDTO> getHisPatients() {
        return hisPatients;
    }

    public void setHisPatients(List<PatientResponseDTO> hisPatients) {
        this.hisPatients = hisPatients;
    }

    private List<PatientResponseDTO> hisPatients;


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
