package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.domain.*;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorDetailsResponseDTO;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorResponseDTO;
import com.example.clinicmanagementsystem.dtos.patients.PatientResponseDTO;

import java.util.List;

public interface IStakeholderService {


    List<DoctorResponseDTO> getAllDoctors();

    List<PatientResponseDTO> getAllPatients();

    DoctorResponseDTO getADoctor(long doctorId);

    DoctorDetailsResponseDTO getFullDoctorData(long doctorId);

    PatientResponseDTO getAPatient(long patientId);

    DoctorResponseDTO addNewDoctor(String firstName, String lastName, String specialization, String contactInfo, String username, String password);

    void updateADoctor(long id, String firstName, String lastName, String specialization, String contactInfo);

    PatientResponseDTO addNewPatient(String firstName, String lastName, String gender, String nationalNumber, String username, String password);

    int removeDoctor(long doctorId);

    boolean removePatient(long patientId);

    List<Patient> getDoctorPatients(long doctorId);

    List<Doctor> getPatientDoctors(long id);

    List<Appointment> getPatientOldAppointments(long patientId);


    void updatePatient(long patientId, String firstName, String lastName, String gender, String nationalNumber);

    Stakeholder getStakeholderByUsername(String username);


}
