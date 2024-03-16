package com.example.clinicmanagementsystem.dtos.appointments;

import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class UpdateAppointmentRequestDTO {

    private long id;

    @NotNull(message = "Appointment Date and Time must be provided!")
    @Future(message = "Your Appointment Should be in future")
    private LocalDateTime appointmentDateTime;
    private String purpose;
    private int doctor;

    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "National Number must be provided e.g 'yy.mm.dd-xxx.cd' ")
    private String patientNN;

    private AppointmentType appointmentType;

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

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
