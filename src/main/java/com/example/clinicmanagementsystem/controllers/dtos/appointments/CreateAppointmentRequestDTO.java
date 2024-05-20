package com.example.clinicmanagementsystem.controllers.dtos.appointments;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class CreateAppointmentRequestDTO {

    @Positive(message = "Appointment Date and Time must be provided!")
    private int appointmentSlotId;
    private String purpose;
    @Positive(message = "Doctor id must be provided!")
    private int doctor;
    @NotNull(message = "National Number must be provided")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "National Number must be provided e.g 'yy.mm.dd-xxx.cd'")
    private String patientNN;


    private String appointmentType;


    public CreateAppointmentRequestDTO(int appointmentSlotId, String purpose, int doctor, String patientNN, String appointmentType) {
        this.appointmentSlotId = appointmentSlotId;
        this.purpose = purpose;
        this.doctor = doctor;
        this.patientNN = patientNN;
        this.appointmentType = appointmentType;
    }


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

    @Override
    public String toString() {
        return "CreateAppointmentRequestDTO{" +
                "appointmentDateTime=" + appointmentSlotId +
                ", purpose='" + purpose + '\'' +
                ", doctor=" + doctor +
                ", patientNN='" + patientNN + '\'' +
                ", appointmentType=" + appointmentType +
                '}';
    }
}
