package com.example.clinicmanagementsystem.Exceptions;

import jakarta.persistence.NoResultException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.webjars.NotFoundException;

public class NationalNumberNotFoundException extends NoResultException {
    public NationalNumberNotFoundException(String nationalNumber) {
        super("Patient with NN: %s NOT FOUND!".formatted(nationalNumber));
    }
}
