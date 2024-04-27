package com.example.clinicmanagementsystem.services.treatementServices;

import com.example.clinicmanagementsystem.exceptions.MedicationDeletionException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.domain.Prescription;
import com.example.clinicmanagementsystem.domain.util.Dosage;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.example.clinicmanagementsystem.domain.util.Unit;
import com.example.clinicmanagementsystem.controllers.dtos.medications.MedicationResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.prescriptions.PrescriptionResponseDTO;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.medicationsRepo.MedicationsSpringData;
import com.example.clinicmanagementsystem.repository.prescriptionRepo.PrescriptionsSpringData;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TreatmentSvc implements ITreatmentService {


    private final PrescriptionsSpringData prescriptionsRepo;
    private final MedicationsSpringData medicationsRepo;

    private final AppointmentsSpringData appointmentsRepo;
    private final ModelMapper modelMapper;


    public TreatmentSvc(PrescriptionsSpringData prescriptionsRepo, MedicationsSpringData medicationsRepo, AppointmentsSpringData appointmentsRepo, ModelMapper modelMapper) {
        this.prescriptionsRepo = prescriptionsRepo;
        this.medicationsRepo = medicationsRepo;
        this.appointmentsRepo = appointmentsRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addNewPrescription(List<Integer> medicationsIds, LocalDate expireDate, long appointmentId) {
        Prescription prescription = new Prescription();
        prescription.setExpireDate(expireDate);
        List<Medication> medications = new ArrayList<>();
        for (Integer medId : medicationsIds) {
            medications.add(medicationsRepo.findById(medId).orElse(null));
        }
        prescription.setMedications(medications);
        Appointment appointment = appointmentsRepo.findById(appointmentId).orElse(null);
        prescription.setAppointment(appointment);
        appointment.setPrescription(prescription);


        prescriptionsRepo.save(prescription);
        appointmentsRepo.save(appointment);
    }

    @Override
    public boolean removePrescription(long prescriptionId) {
        prescriptionsRepo.findById(prescriptionId).ifPresent(prescription -> prescription.getAppointment().setPrescription(null));
        prescriptionsRepo.deleteById(prescriptionId);
        return prescriptionsRepo.findById(prescriptionId).isEmpty();
    }

    @Override
    public boolean changePrescription(long prescriptionId, List<Medication> medications, LocalDate expireDate) {
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
    public PrescriptionResponseDTO getPrescription(long prescriptionId) {
        Prescription prescription = prescriptionsRepo.findById(prescriptionId).orElse(null);
        return modelMapper.map(prescription, PrescriptionResponseDTO.class);
    }

    @Override
    public Medication addNewMedication(String name, MedicationForm type, int quantity, Unit unit, int frequencies, int daysDuration, String notes) {
        Medication medication = new Medication();
        medication.setName(name);
        medication.setForm(type);
        medication.setDosage(new Dosage(unit, quantity));
        medication.setFrequencies(frequencies);
        medication.setDaysDuration(daysDuration);
        medication.setNotes(notes);
        return medicationsRepo.save(medication);
    }

    @Override
    public Medication updateMedication(int medId, String name, MedicationForm type, int quantity, Unit unit, int frequencies, int daysDuration, String notes) {
        Medication medication = new Medication();
        medication.setMedicationId(medId);
        medication.setName(name);
        medication.setForm(type);
        medication.setDosage(new Dosage(unit, quantity));
        medication.setFrequencies(frequencies);
        medication.setDaysDuration(daysDuration);
        medication.setNotes(notes);
        return medicationsRepo.save(medication);
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
    public List<MedicationResponseDTO> getAllMedications() {
        List<Medication> medications = medicationsRepo.findAll();

        List<MedicationResponseDTO> medicationResponseDTOS = new ArrayList<>();
        for (Medication medication : medications) {

            MedicationResponseDTO medicationResponseDTO = modelMapper.map(medication, MedicationResponseDTO.class);
            medicationResponseDTO.setUsage(medicationResponseDTO.getForm().getUsage());
            medicationResponseDTOS.add(medicationResponseDTO);
        }
        return medicationResponseDTOS;
    }

    @Override
    public List<Medication> getPrescriptionMedication(int prescriptionId) {
        return medicationsRepo.getPrescriptionMedication(prescriptionId);
    }

    @Override
    public MedicationResponseDTO getMedication(int medicationId) {
        Medication medication = medicationsRepo.findById(medicationId).orElse(null);
        MedicationResponseDTO medicationResponseDTO = modelMapper.map(medication, MedicationResponseDTO.class);
        medicationResponseDTO.setUsage(medicationResponseDTO.getForm().getUsage());
        return medicationResponseDTO;
    }

    @Override
    public List<PrescriptionResponseDTO> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionsRepo.findAll();
        List<PrescriptionResponseDTO> prescriptionResponseDTOS = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            prescriptionResponseDTOS.add(modelMapper.map(prescription, PrescriptionResponseDTO.class));
        }

        return prescriptionResponseDTOS;
    }
}
