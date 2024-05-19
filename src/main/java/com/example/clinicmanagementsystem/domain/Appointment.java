package com.example.clinicmanagementsystem.domain;

import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"slot_id", "doctor_id", "patient_id"}),
        @UniqueConstraint(columnNames = {"slot_id", "doctor_id"}),
        @UniqueConstraint(columnNames = {"slot_id", "patient_id"})
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long appointmentId;
    @OneToOne
    @JoinColumn(name = "slot_id")
    @JsonIgnoreProperties("doctor") // Prevents back-serialization to Doctor
    private Availability availabilitySlot;
    private String purpose;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;


    public Appointment() {
    }


    public Appointment(int appointmentId, Availability availabilitySlot, String purpose, Doctor doctor, Patient patient, AppointmentType appointmentType) {
        this.appointmentId = appointmentId;
        this.availabilitySlot = availabilitySlot;
        this.purpose = purpose;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentType = appointmentType;
    }

    public Appointment(int appointmentId, Availability availabilitySlot, String purpose, AppointmentType appointmentType) {
        this.appointmentId = appointmentId;
        this.availabilitySlot = availabilitySlot;
        this.purpose = purpose;
        this.appointmentType = appointmentType;
    }

    public Appointment(Availability availabilitySlot, String purpose, Doctor doctor, Patient patient, AppointmentType appointmentType) {
        this.availabilitySlot = availabilitySlot;
        this.purpose = purpose;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentType = appointmentType;
    }


    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Availability getAvailabilitySlot() {
        return availabilitySlot;
    }

    public void setAvailabilitySlot(Availability appointmentDateTime) {
        this.availabilitySlot = appointmentDateTime;
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

    public boolean isDone() {
        return this.availabilitySlot.getSlot().isBefore(LocalDateTime.now());
    }


    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentDateTime=" + availabilitySlot +
                ", purpose='" + purpose + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", prescription=" + prescription +
                ", appointmentType=" + appointmentType +
                '}';
    }
}
