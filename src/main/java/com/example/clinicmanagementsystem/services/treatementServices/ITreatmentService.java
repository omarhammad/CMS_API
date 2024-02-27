package com.example.clinicmanagementsystem.services.treatementServices;

import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.example.clinicmanagementsystem.domain.Prescription;
import com.example.clinicmanagementsystem.domain.util.Unit;

import java.time.LocalDate;
import java.util.List;

public interface ITreatmentService {


    boolean addNewPrescription(List<Medication> medications, LocalDate expireDate, int appointmentId);

    boolean removePrescription(int prescriptionId);

    boolean changePrescription(int prescriptionId, List<Medication> medications, LocalDate expireDate);


    Prescription getPrescription(int prescriptionId);

    boolean addNewMedication(String name, MedicationForm type, int quantity, Unit unit, int frequencies, int daysDuration, String notes);

    boolean removeMedication(int medicationId);

    List<Medication> getAllMedications();

    List<Medication> getPrescriptionMedication(int prescriptionId);

    Medication getMedication(int medicationId);


}
