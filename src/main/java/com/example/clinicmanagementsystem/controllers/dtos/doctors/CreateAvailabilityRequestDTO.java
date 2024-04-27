package com.example.clinicmanagementsystem.controllers.dtos.doctors;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateAvailabilityRequestDTO {
    @NotNull(message = "Slot Date and Time must be provided!")
    @Future(message = "Your Slot Should be in future")
    private LocalDateTime slot;

    public CreateAvailabilityRequestDTO() {
    }

    public CreateAvailabilityRequestDTO(LocalDateTime slot) {
        this.slot = slot;
    }

    public LocalDateTime getSlot() {
        return slot;
    }

    public void setSlot(LocalDateTime slot) {
        this.slot = slot;
    }


    @Override
    public String toString() {
        return "CreateAvailabilityRequestDTO{" +
                "slot=" + slot +
                '}';
    }
}
