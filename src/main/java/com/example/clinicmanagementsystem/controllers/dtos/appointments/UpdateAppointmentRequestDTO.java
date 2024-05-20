package com.example.clinicmanagementsystem.controllers.dtos.appointments;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class UpdateAppointmentRequestDTO {

    private long appointmentId;

    @Positive(message = "Appointment Date and Time must be provided!")
    private int appointmentSlotId;
    private String purpose;
    @Positive(message = "Doctor id must be provided!")
    private int doctor;

    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "National Number must be provided e.g 'yy.mm.dd-xxx.cd' ")
    private String patientNN;

    private String appointmentType;


    @NotNull(message = "Appointment Date and Time must be provided!")
    public int getAppointmentSlotId() {
        return appointmentSlotId;
    }

    public void setAppointmentSlotId(@NotNull(message = "Appointment Date and Time must be provided!") int appointmentSlotId) {
        this.appointmentSlotId = appointmentSlotId;
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

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
