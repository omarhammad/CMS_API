package com.example.clinicmanagementsystem.domain;

import com.example.clinicmanagementsystem.domain.util.Dosage;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medicationId;

    @CsvBindByName(column = "Name")
    private String name;

    @Enumerated(EnumType.STRING)
    @CsvBindByName(column = "Form")
    private MedicationForm form;

    @Embedded
    @CsvRecurse
    private Dosage dosage;

    @CsvBindByName(column = "Frequency")
    private int frequencies;

    @CsvBindByName(column = "Days Duration")
    private int daysDuration;

    @CsvBindByName(column = "Notes")
    private String notes;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;

    public Medication() {
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
