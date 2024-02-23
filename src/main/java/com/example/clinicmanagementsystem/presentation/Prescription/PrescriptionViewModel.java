package com.example.clinicmanagementsystem.presentation.Prescription;

import com.example.clinicmanagementsystem.domain.Medication;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionViewModel {

    @NotNull(message = "Expire Date must be provided!")
    @Future(message = "Expire Date must be in future!")
    private LocalDate expireDate;
    @NotEmpty(message = "At least one Medication must be provided!")
    private List<Integer> selectedMedications;


    public PrescriptionViewModel() {
    }


    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public List<Integer> getSelectedMedications() {
        return selectedMedications;
    }

    public void setSelectedMedications(List<Integer> selectedMedications) {
        this.selectedMedications = selectedMedications;
    }
}

