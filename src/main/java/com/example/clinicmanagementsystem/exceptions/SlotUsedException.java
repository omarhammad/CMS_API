package com.example.clinicmanagementsystem.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class SlotUsedException extends DataIntegrityViolationException {
    public SlotUsedException(String chooseAnotherSlot) {
        super(chooseAnotherSlot);
    }
}
