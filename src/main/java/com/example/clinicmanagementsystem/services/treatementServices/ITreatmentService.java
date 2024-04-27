package com.example.clinicmanagementsystem.services.treatementServices;

import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.example.clinicmanagementsystem.domain.util.Unit;
import com.example.clinicmanagementsystem.controllers.dtos.medications.MedicationResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.prescriptions.PrescriptionResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ITreatmentService {


    void addNewPrescription(List<Integer> medications, LocalDate expireDate, long appointmentId);

    boolean removePrescription(long prescriptionId);

    boolean changePrescription(long prescriptionId, List<Medication> medications, LocalDate expireDate);


    PrescriptionResponseDTO getPrescription(long prescriptionId);

    Medication addNewMedication(String name, MedicationForm type, int quantity, Unit unit, int frequencies, int daysDuration, String notes);

    Medication updateMedication(int medId, String name, MedicationForm type, int quantity, Unit unit, int frequencies, int daysDuration, String notes);


    boolean removeMedication(int medicationId);

    List<MedicationResponseDTO> getAllMedications();

    List<Medication> getPrescriptionMedication(int prescriptionId);

    MedicationResponseDTO getMedication(int medicationId);


    List<PrescriptionResponseDTO> getAllPrescriptions();
}
