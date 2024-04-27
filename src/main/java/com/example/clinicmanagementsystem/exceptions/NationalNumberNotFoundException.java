package com.example.clinicmanagementsystem.exceptions;

import jakarta.persistence.NoResultException;

public class NationalNumberNotFoundException extends NoResultException {
    public NationalNumberNotFoundException(String nationalNumber) {
        super("Patient with NN: %s NOT FOUND!".formatted(nationalNumber));
    }
}
