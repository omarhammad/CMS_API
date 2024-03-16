package com.example.clinicmanagementsystem.dtos.appointments;

import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.Prescription;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private long appointmentId;

    private LocalDateTime appointmentDateTime;

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

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
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
}
