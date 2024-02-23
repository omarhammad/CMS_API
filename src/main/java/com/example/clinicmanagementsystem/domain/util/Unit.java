package com.example.clinicmanagementsystem.domain.util;

public enum Unit {

    ML("milliliters"), MG("milligram");

    private String label;


    Unit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
