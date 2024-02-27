package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorResponseDTO;

import java.util.List;

public interface IStakeholderService {


    List<DoctorResponseDTO> getAllDoctors();

    List<Patient> getAllPatients();

    DoctorResponseDTO getADoctor(int doctorId);

    Patient getAPatient(int patientId);

    DoctorResponseDTO addNewDoctor(String firstName, String lastName, String specialization, String contactInfo);


    boolean addNewPatient(String firstName, String lastName, String gender, String nationalNumber);

    int removeDoctor(int doctorId);

    boolean removePatient(int patientId);

    List<Patient> getDoctorPatients(int doctorId);

    List<Doctor> getPatientDoctors(int id);

    List<Appointment> getPatientOldAppointments(int patientId);
}
