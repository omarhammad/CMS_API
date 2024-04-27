package com.example.clinicmanagementsystem.exceptions;

import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;

public class NationalNumberExistException extends ConstraintViolationException {

    private final String msg;

    public NationalNumberExistException(String nationalNumber) {
        super("", new SQLException("sdf"), "");
        this.msg = "Patient with NN: %s Already Exist!".formatted(nationalNumber);
    }


    @Override
    public String getMessage() {
        return this.msg;
    }
}
