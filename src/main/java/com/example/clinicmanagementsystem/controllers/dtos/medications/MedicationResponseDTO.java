package com.example.clinicmanagementsystem.controllers.dtos.medications;

import com.example.clinicmanagementsystem.domain.util.Dosage;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;

public class MedicationResponseDTO {

    private int medicationId;
    private String name;
    private MedicationForm form;
    private String usage;
    private Dosage dosage;
    private int frequencies;
    private int daysDuration;
    private String notes;


    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MedicationForm getForm() {
        return form;
    }

    public void setForm(MedicationForm form) {
        this.form = form;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public void setDosage(Dosage dosage) {
        this.dosage = dosage;
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

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
