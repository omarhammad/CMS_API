package com.example.clinicmanagementsystem.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class InvalidAppointmentException extends DataIntegrityViolationException {

    public InvalidAppointmentException(String message) {
        super(message);
    }
}
