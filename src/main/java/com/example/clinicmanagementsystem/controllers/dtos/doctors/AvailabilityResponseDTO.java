package com.example.clinicmanagementsystem.controllers.dtos.doctors;

import com.example.clinicmanagementsystem.domain.Doctor;

import java.time.LocalDateTime;

public class AvailabilityResponseDTO {


    private int id;

    private LocalDateTime slot;

    private DoctorResponseDTO doctor;


    private boolean used;

    public AvailabilityResponseDTO() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getSlot() {
        return slot;
    }

    public void setSlot(LocalDateTime slot) {
        this.slot = slot;
    }

    public DoctorResponseDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorResponseDTO doctor) {
        this.doctor = doctor;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "AvailabilityResponseDTO{" +
                "id=" + id +
                ", slot=" + slot +
                ", doctor=" + doctor +
                ", used=" + used +
                '}';
    }
}
