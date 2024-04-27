package com.example.clinicmanagementsystem.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ContactInfoExistException extends DataIntegrityViolationException {
    public ContactInfoExistException(String contactInfo) {
        super("[%s][%s] Contacts already exist!".formatted(contactInfo.split(",")[0], contactInfo.split(",")[1]));
    }
}
