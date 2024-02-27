package com.example.clinicmanagementsystem.controllers.appointment;

import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class AppointmentViewModel {
    private int doctorId;

    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "National Number must be provided e.g 'yy.mm.dd-xxx.cd' ")
    private String patientNationalNumber;

    @NotNull(message = "Appointment Date and Time must be provided!")
    @Future(message = "Your Appointment Should be in future")
    private LocalDateTime appointmentDateTime;

    private String purpose;

    private AppointmentType appointmentType;


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientNationalNumber() {
        return patientNationalNumber;
    }

    public void setPatientNationalNumber(String patientNationalNumber) {
        this.patientNationalNumber = patientNationalNumber;

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

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public String toString() {
        return "AddAppointmentModel{" +
                "doctorId=" + doctorId +
                ", patientNationalNumber='" + patientNationalNumber + '\'' +
                ", appointmentDateTime=" + appointmentDateTime +
                ", purpose='" + purpose + '\'' +
                ", appointmentType=" + appointmentType +
                '}';
    }
}
