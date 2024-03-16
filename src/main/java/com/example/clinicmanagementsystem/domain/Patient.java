package com.example.clinicmanagementsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.intellij.lang.annotations.RegExp;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient extends Stakeholder {


    @Column(unique = true)
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$")
    private String nationalNumber;
    private int age;
    private String gender;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    private List<Appointment> appointments;


    public Patient() {
        appointments = new ArrayList<>();
    }

    public Patient(int id) {
        super(id);
    }

    public Patient(int id, String firstName, String lastName, String nationalNumber, int age, String gender) {
        super(id);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.nationalNumber = nationalNumber;
        this.age = age;
        this.gender = gender;
    }

    public Patient(String firstName, String lastName, String nationalNumber, int age, String gender) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.nationalNumber = nationalNumber;
        this.age = age;
        this.gender = gender;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }


    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
