package com.example.clinicmanagementsystem.controllers.dtos.medications;

import com.example.clinicmanagementsystem.domain.util.Unit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateMedicationRequestDTO {

    @NotNull
    @NotBlank(message = "The medication name must be provided!")
    private String name;


    @NotNull(message = "Medication form must be provided!")
    private String medicationForm;
    @Min(value = 1, message = "Dosage can't be negative or Zero!")
    private int quantity;
    @NotNull(message = "you must specify the unit for the medication!")
    private String unit;
    @Min(value = 1, message = "Freq can't be negative or Zero!")
    private int frequencies;
    @Min(value = 1, message = "Duration of days can't be negative or Zero!")
    private int daysDuration;

    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicationForm() {
        return medicationForm;
    }

    public void setMedicationForm(String medicationForm) {
        this.medicationForm = medicationForm;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(int frequencies) {
        this.frequencies = frequencies;
    }

    public int getDaysDuration() {
        return daysDuration;
    }

    public void setDaysDuration(int daysDuration) {
        this.daysDuration = daysDuration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
