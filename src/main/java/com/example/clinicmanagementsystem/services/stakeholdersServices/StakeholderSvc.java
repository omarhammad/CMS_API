package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.Exceptions.ContactInfoExistException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberExistException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsJPA2;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersJPA2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StakeholderJPA2Service implements StakeholderService {


    StakeholdersJPA2 stakeholdersRepo;
    AppointmentsJPA2 appointmentsRepo;

    public StakeholderJPA2Service(StakeholdersJPA2 stakeholdersRepo, AppointmentsJPA2 appointmentsRepo) {
        this.stakeholdersRepo = stakeholdersRepo;
        this.appointmentsRepo = appointmentsRepo;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return stakeholdersRepo.findAll().stream()
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .collect(Collectors.toList());

    }

    @Override
    public List<Patient> getAllPatients() {
        return stakeholdersRepo.findAll().stream()
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Doctor getADoctor(int doctorId) {
        return ((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
    }

    @Override
    public Patient getAPatient(int patientId) {
        return ((Patient) stakeholdersRepo.findById(patientId).orElse(null));
    }

    @Override
    public boolean addNewDoctor(String firstName, String lastName, String specialization, String contactInfo) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialization(specialization);
        doctor.setContactInfo(contactInfo);

        try {
            stakeholdersRepo.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new ContactInfoExistException(contactInfo);
        }
        return true;
    }

    @Override
    public boolean addNewPatient(String firstName, String lastName, String gender, String nationalNumber) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setGender(gender);
        patient.setNationalNumber(nationalNumber);

        String[] nDateOfBirth = nationalNumber.split("-")[0].split("\\.");
        int currentYearDigits = LocalDate.now().getYear() % 100;
        int century = (Integer.parseInt(nDateOfBirth[0]) <= currentYearDigits) ? 2000 : 1900;
        int year = century + Integer.parseInt(nDateOfBirth[0]);
        LocalDate patientDob = LocalDate.of(year, Integer.parseInt(nDateOfBirth[1]), Integer.parseInt(nDateOfBirth[2]));
        LocalDate current = LocalDate.now();
        int age = Period.between(patientDob, current).getYears();
        patient.setAge(age);
        try {
            stakeholdersRepo.save(patient);
        } catch (DataIntegrityViolationException e) {
            throw new NationalNumberExistException(nationalNumber);
        }
        return true;
    }

    @Override
    public boolean removeDoctor(int doctorId) {
        stakeholdersRepo.deleteById(doctorId);
        return stakeholdersRepo.findById(doctorId).isEmpty();
    }

    @Override
    public boolean removePatient(int patientId) {
        stakeholdersRepo.deleteById(patientId);
        return stakeholdersRepo.findById(patientId).isEmpty();
    }

    @Override
    public List<Patient> getDoctorPatients(int doctorId) {
        return stakeholdersRepo.findDoctorPatients(doctorId);
    }

    @Override
    public List<Doctor> getPatientDoctors(int patientId) {
        return stakeholdersRepo.findPatientDoctors(patientId);
    }

    @Override
    public List<Appointment> getPatientOldAppointments(int patientId) {
        return appointmentsRepo.getPatientOldAppointments(patientId);
    }
}
