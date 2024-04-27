package com.example.clinicmanagementsystem.exceptions;

import jakarta.persistence.NoResultException;

public class DoctorNotFoundException extends NoResultException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
