package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.Exceptions.ContactInfoExistException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberExistException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorResponseDTO;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StakeholderSvc implements IStakeholderService {


    StakeholdersSpringData stakeholdersRepo;
    AppointmentsSpringData appointmentsRepo;
    ModelMapper modelMapper;

    public StakeholderSvc(StakeholdersSpringData stakeholdersRepo, AppointmentsSpringData appointmentsRepo, ModelMapper modelMapper) {
        this.stakeholdersRepo = stakeholdersRepo;
        this.appointmentsRepo = appointmentsRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = stakeholdersRepo.findAll().stream()
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .toList();

        List<DoctorResponseDTO> doctorResponseDTOS = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorResponseDTOS.add(modelMapper.map(doctor, DoctorResponseDTO.class));
        }
        return doctorResponseDTOS;

    }

    @Override
    public List<Patient> getAllPatients() {
        return stakeholdersRepo.findAll().stream()
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDTO getADoctor(int doctorId) {
        Doctor doctor = ((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        if (doctor == null) {
            return null;
        }
        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }

    @Override
    public Patient getAPatient(int patientId) {
        return ((Patient) stakeholdersRepo.findById(patientId).orElse(null));
    }

    @Override
    public DoctorResponseDTO addNewDoctor(String firstName, String lastName, String specialization, String contactInfo) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialization(specialization);
        doctor.setContactInfo(contactInfo);

        Doctor addedDoctor;

        try {
            addedDoctor = stakeholdersRepo.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new ContactInfoExistException(contactInfo);
        }

        return modelMapper.map(addedDoctor, DoctorResponseDTO.class);
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
    public int removeDoctor(int doctorId) {
        if (stakeholdersRepo.findById(doctorId).isEmpty()) {
            return 404;
        }
        stakeholdersRepo.deleteById(doctorId);
        return stakeholdersRepo.findById(doctorId).isEmpty() ? 204 : 501;
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
