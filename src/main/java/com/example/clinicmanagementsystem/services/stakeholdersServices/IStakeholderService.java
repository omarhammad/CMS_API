package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.domain.*;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorDetailsResponseDTO;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorResponseDTO;
import com.example.clinicmanagementsystem.dtos.patients.PatientResponseDTO;

import java.util.List;

public interface IStakeholderService {


    List<DoctorResponseDTO> getAllDoctors();

    List<PatientResponseDTO> getAllPatients();

    DoctorResponseDTO getADoctor(int doctorId);

    DoctorDetailsResponseDTO getFullDoctorData(int doctorId);

    PatientResponseDTO getAPatient(int patientId);

    DoctorResponseDTO addNewDoctor(String firstName, String lastName, String specialization, String contactInfo, String username, String password);

    void updateADoctor(int id, String firstName, String lastName, String specialization, String contactInfo);

    PatientResponseDTO addNewPatient(String firstName, String lastName, String gender, String nationalNumber, String username, String password);

    int removeDoctor(int doctorId);

    boolean removePatient(int patientId);

    List<Patient> getDoctorPatients(int doctorId);

    List<Doctor> getPatientDoctors(int id);

    List<Appointment> getPatientOldAppointments(int patientId);


    void updatePatient(int patientId, String firstName, String lastName, String gender, String nationalNumber);

    Stakeholder getStakeholderByUsername(String username);


}
