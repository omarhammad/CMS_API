package com.example.clinicmanagementsystem.controllers.dtos.appointments;

import com.example.clinicmanagementsystem.domain.Availability;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.Prescription;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentResponseDTO {

    private long appointmentId;

    @JsonIgnoreProperties(value = {"doctor"})
    private Availability availabilitySlot;


    private Doctor doctor;
    private Patient patient;
    private String purpose;

    private Prescription prescription;

    private AppointmentType appointmentType;


    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Availability getAvailabilitySlot() {
        return availabilitySlot;
    }

    public void setAvailabilitySlot(Availability availabilitySlot) {
        this.availabilitySlot = availabilitySlot;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentResponseDTO that = (AppointmentResponseDTO) o;
        return appointmentId == that.appointmentId && Objects.equals(availabilitySlot, that.availabilitySlot) && Objects.equals(doctor, that.doctor) && Objects.equals(patient, that.patient) && Objects.equals(purpose, that.purpose) && Objects.equals(prescription, that.prescription) && appointmentType == that.appointmentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId, availabilitySlot, doctor, patient, purpose, prescription, appointmentType);
    }

    @Override
    public String toString() {
        return "AppointmentResponseDTO{" +
                "appointmentId=" + appointmentId +
                ", appointmentDateTime=" + availabilitySlot.getSlot() +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", purpose='" + purpose + '\'' +
                ", prescription=" + prescription +
                ", appointmentType=" + appointmentType +
                '}';
    }
}
