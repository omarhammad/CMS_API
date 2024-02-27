package com.example.clinicmanagementsystem.domain;

import com.example.clinicmanagementsystem.domain.util.Dosage;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medicationId;
    private String name;
    @Enumerated(EnumType.STRING)
    private MedicationForm form;
    @Embedded
    private Dosage dosage;
    private int frequencies;
    private int daysDuration;
    private String notes;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;


    public Medication() {
    }

    public Medication(int medicationId, String name, MedicationForm form, Dosage dosage, int frequencies, int daysDuration, String notes) {
        this.medicationId = medicationId;
        this.name = name;
        this.form = form;
        this.dosage = dosage;
        this.frequencies = frequencies;
        this.daysDuration = daysDuration;
        this.notes = notes;
    }

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

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
