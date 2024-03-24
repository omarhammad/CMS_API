package com.example.clinicmanagementsystem.dtos.patients;

import com.example.clinicmanagementsystem.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorResponseDTO;

import java.util.List;

public class PatientResponseDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private String nationalNumber;

    private List<DoctorResponseDTO> hisDoctors;
    private List<AppointmentResponseDTO> oldAppointments;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
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

    public List<DoctorResponseDTO> getHisDoctors() {
        return hisDoctors;
    }

    public void setHisDoctors(List<DoctorResponseDTO> hisDoctors) {
        this.hisDoctors = hisDoctors;
    }

    public List<AppointmentResponseDTO> getOldAppointments() {
        return oldAppointments;
    }

    public void setOldAppointments(List<AppointmentResponseDTO> oldAppointments) {
        this.oldAppointments = oldAppointments;
    }
}
