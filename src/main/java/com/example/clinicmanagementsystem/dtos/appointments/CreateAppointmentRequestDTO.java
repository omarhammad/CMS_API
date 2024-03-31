package com.example.clinicmanagementsystem.dtos.appointments;

import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.Prescription;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class CreateAppointmentRequestDTO {

    @NotNull(message = "Appointment Date and Time must be provided!")
    @Future(message = "Your Appointment Should be in future")
    private LocalDateTime appointmentDateTime;
    private String purpose;
    private int doctor;
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "National Number must be provided e.g 'yy.mm.dd-xxx.cd' ")
    private String patientNN;

    private String appointmentType;


    public CreateAppointmentRequestDTO() {

    }

    public CreateAppointmentRequestDTO(LocalDateTime appointmentDateTime, String purpose, int doctor, String patientNN, String appointmentType) {
        this.appointmentDateTime = appointmentDateTime;
        this.purpose = purpose;
        this.doctor = doctor;
        this.patientNN = patientNN;
        this.appointmentType = appointmentType;
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

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public String getPatientNN() {
        return patientNN;
    }

    public void setPatientNN(String patientNN) {
        this.patientNN = patientNN;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public String toString() {
        return "CreateAppointmentRequestDTO{" +
                "appointmentDateTime=" + appointmentDateTime +
                ", purpose='" + purpose + '\'' +
                ", doctor=" + doctor +
                ", patientNN='" + patientNN + '\'' +
                ", appointmentType=" + appointmentType +
                '}';
    }
}
