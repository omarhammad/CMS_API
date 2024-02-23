package com.example.clinicmanagementsystem.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ContactInfoExistException extends DataIntegrityViolationException {
    public ContactInfoExistException(String contactInfo) {
        super("%s already exist!".formatted(contactInfo));
    }
}
