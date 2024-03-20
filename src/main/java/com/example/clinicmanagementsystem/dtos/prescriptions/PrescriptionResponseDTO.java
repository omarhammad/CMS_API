package com.example.clinicmanagementsystem.dtos.prescriptions;

import com.example.clinicmanagementsystem.domain.Medication;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionResponseDTO {

    private int prescriptionId;
    private LocalDate expireDate;
    private List<Medication> medications;

    public PrescriptionResponseDTO() {
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
