package com.example.clinicmanagementsystem.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class MedicationDeletionException extends DataIntegrityViolationException {
    public MedicationDeletionException(String medicationName) {
        super("The deletion of %s can't be done as some Prescriptions use it !".formatted(medicationName));
    }
}
