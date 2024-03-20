package com.example.clinicmanagementsystem.dtos.prescriptions;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionsRequestDTO {


    @NotNull(message = "Expire Date must be provided!")
    @Future(message = "Expire Date must be in future!")
    private LocalDate expireDate;
    @NotEmpty(message = "At least one Medication must be provided!")
    private List<Integer> medicationsIds;

    private int appointmentId;


    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public List<Integer> getMedicationsIds() {
        return medicationsIds;
    }

    public void setMedicationsIds(List<Integer> medicationsIds) {
        this.medicationsIds = medicationsIds;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public String toString() {
        return "PrescriptionsRequestDTO{" +
                "expireDate=" + expireDate +
                ", medicationsIds=" + medicationsIds +
                ", appointmentId=" + appointmentId +
                '}';
    }
}
