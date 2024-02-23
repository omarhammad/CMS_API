package com.example.clinicmanagementsystem.domain.util;

public enum AppointmentType {
    CONSULTATION("Consultation"), FOLLOW_UP("Follow-up"), EMERGENCY("Emergency"), DIAGNOSTIC_TESTING("Diagnostic Testing");

    private final String label;

    AppointmentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}