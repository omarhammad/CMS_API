package com.example.clinicmanagementsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor extends Stakeholder {


    @Column(nullable = false)
    @Size(min = 3, max = 20)
    private String firstName;
    @Column(nullable = false)
    @Size(max = 30)
    private String lastName;
    @Column(nullable = false)
    private String specialization;
    @Column(nullable = false, unique = true)
    private String contactInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    private List<Appointment> appointment;


    public Doctor() {
        appointment = new ArrayList<>();
    }

    public Doctor(int id) {
        super(id);
    }

    public Doctor(int id, String firstName, String lastName, String specialization, String contactInfo) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
    }

    public Doctor(String firstName, String lastName, String specialization, String contactInfo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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


    public List<Appointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(List<Appointment> appointment) {
        this.appointment = appointment;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
