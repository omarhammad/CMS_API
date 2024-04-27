package com.example.clinicmanagementsystem.exceptions;

import jakarta.persistence.NoResultException;

public class AppointmentNotFoundException extends NoResultException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
