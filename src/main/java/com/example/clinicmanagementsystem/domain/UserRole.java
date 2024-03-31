package com.example.clinicmanagementsystem.domain;

public enum UserRole {


    DOCTOR("ROLE_DOCTOR"), PATIENT("ROLE_PATIENT"), SECRETARY("ROLE_SECRETARY"), ADMIN("ROLE_ADMIN");


    private final String label;

    UserRole(String label) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }

}
