package com.example.clinicmanagementsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor extends Stakeholder {


    @Column(nullable = false)
    private String specialization;
    @Column(nullable = false, unique = true)
    private String contactInfo;
    @JsonIgnoreProperties("doctor") // Prevents back-serialization to Doctor
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    private List<Availability> availability;


    @JsonIgnoreProperties("doctor") // Prevents back-serialization to Doctor
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    private List<Appointment> appointments;


    public Doctor() {
        appointments = new ArrayList<>();
    }

    public Doctor(int id) {
        super(id, UserRole.DOCTOR);
    }

    public Doctor(int id, String firstName, String lastName, String specialization, String contactInfo) {
        super(id, UserRole.DOCTOR);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.specialization = specialization;
        this.contactInfo = contactInfo;
    }

    public Doctor(String firstName, String lastName, String specialization, String contactInfo) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.specialization = specialization;
        this.contactInfo = contactInfo;
    }


    public String getFirstName() {
        return super.getFirstName();
    }

    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    public String getLastName() {
        return super.getLastName();
    }

    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<Availability> getAvailability() {
        return availability;
    }

    public void setAvailability(List<Availability> availability) {
        this.availability = availability;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointment) {
        this.appointments = appointment;
    }


    @Override
    public String toString() {
        return "Doctor{" +
                "specialization='" + specialization + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
