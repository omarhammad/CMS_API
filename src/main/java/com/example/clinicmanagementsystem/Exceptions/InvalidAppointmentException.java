package com.example.clinicmanagementsystem.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvalidAppointmentException extends DataIntegrityViolationException {

    public InvalidAppointmentException(String patientName, String doctorName, String appointmentDateTime) {
        super("Invalid appointment because,  Dr.%s and Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(doctorName, patientName, appointmentDateTime));
    }
}
