package com.example.clinicmanagementsystem.services.treatementServices;

import com.example.clinicmanagementsystem.Exceptions.MedicationDeletionException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.domain.Prescription;
import com.example.clinicmanagementsystem.domain.util.Dosage;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.example.clinicmanagementsystem.domain.util.Unit;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsJPA2;
import com.example.clinicmanagementsystem.repository.medicationsRepo.MedicationsJPA2;
import com.example.clinicmanagementsystem.repository.prescriptionRepo.PrescriptionsJPA2;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TreatmentJP2Service implements TreatmentService {


    PrescriptionsJPA2 prescriptionsRepo;
    MedicationsJPA2 medicationsRepo;

    AppointmentsJPA2 appointmentsRepo;

    public TreatmentJP2Service(PrescriptionsJPA2 prescriptionsRepo, MedicationsJPA2 medicationsRepo, AppointmentsJPA2 appointmentsRepo) {
        this.prescriptionsRepo = prescriptionsRepo;
        this.medicationsRepo = medicationsRepo;
        this.appointmentsRepo = appointmentsRepo;
    }

    @Override
    public boolean addNewPrescription(List<Medication> medications, LocalDate expireDate, int appointmentId) {
        Prescription prescription = new Prescription();
        prescription.setExpireDate(expireDate);
        prescription.setMedications(medications);
        Appointment appointment = appointmentsRepo.findById(appointmentId).orElse(null);
        prescription.setAppointment(appointment);
        appointment.setPrescription(prescription);


        prescriptionsRepo.save(prescription);
        appointmentsRepo.save(appointment);
        return true;
    }

    @Override
    public boolean removePrescription(int prescriptionId) {
        prescriptionsRepo.findById(prescriptionId).ifPresent(prescription -> prescription.getAppointment().setPrescription(null));
        prescriptionsRepo.deleteById(prescriptionId);
        return prescriptionsRepo.findById(prescriptionId).isEmpty();
    }

    @Override
    public boolean changePrescription(int prescriptionId, List<Medication> medications, LocalDate expireDate) {
        Prescription prescription = prescriptionsRepo.findById(prescriptionId).orElse(null);
        if (prescription != null) {
            prescription.setMedications(medications);
            prescription.setExpireDate(expireDate);
            prescriptionsRepo.save(prescription);
            return true;
        }
        return false;
    }

    @Override
    public Prescription getPrescription(int prescriptionId) {
        return prescriptionsRepo.findById(prescriptionId).orElse(null);
    }

    @Override
    public boolean addNewMedication(String name, MedicationForm type, int quantity, Unit unit, int frequencies, int daysDuration, String notes) {
        Medication medication = new Medication();
        medication.setName(name);
        medication.setForm(type);
        medication.setDosage(new Dosage(unit, quantity));
        medication.setFrequencies(frequencies);
        medication.setDaysDuration(daysDuration);
        medication.setNotes(notes);
        medicationsRepo.save(medication);
        return true;
    }

    @Override
    public boolean removeMedication(int medicationId) {
        try {
            medicationsRepo.deleteById(medicationId);
        } catch (DataIntegrityViolationException e) {
            String name = Objects.requireNonNull(medicationsRepo.findById(medicationId).orElse(null)).getName();
            throw new MedicationDeletionException(name);
        }
        return medicationsRepo.findById(medicationId).isEmpty();
    }

    @Override
    public List<Medication> getAllMedications() {
        return medicationsRepo.findAll();
    }

    @Override
    public List<Medication> getPrescriptionMedication(int prescriptionId) {
        return medicationsRepo.getPrescriptionMedication(prescriptionId);
    }

    @Override
    public Medication getMedication(int medicationId) {
        return medicationsRepo.findById(medicationId).orElse(null);
    }
}
