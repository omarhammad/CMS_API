package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;

import java.util.List;

public interface StakeholderService {


    List<Doctor> getAllDoctors();

    List<Patient> getAllPatients();

    Doctor getADoctor(int doctorId);

    Patient getAPatient(int patientId);

    boolean addNewDoctor(String firstName, String lastName, String specialization, String contactInfo);


    boolean addNewPatient(String firstName, String lastName, String gender, String nationalNumber);

    boolean removeDoctor(int doctorId);

    boolean removePatient(int patientId);

    List<Patient> getDoctorPatients(int doctorId);

    List<Doctor> getPatientDoctors(int id);

    List<Appointment> getPatientOldAppointments(int patientId);
}
