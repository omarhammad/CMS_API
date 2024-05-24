package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.controllers.dtos.doctors.AvailabilityResponseDTO;
import com.example.clinicmanagementsystem.domain.*;
import com.example.clinicmanagementsystem.controllers.dtos.doctors.DoctorDetailsResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.doctors.DoctorResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.patients.PatientResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IStakeholderService {


    List<DoctorResponseDTO> getAllDoctors();

    List<PatientResponseDTO> getAllPatients();

    DoctorResponseDTO getADoctor(long doctorId);

    DoctorDetailsResponseDTO getFullDoctorData(long doctorId);

    PatientResponseDTO getAPatient(long patientId);

    DoctorResponseDTO addNewDoctor(String firstName, String lastName, String specialization, String contactInfo, String username, String password);

    void updateADoctor(long id, String firstName, String lastName, String specialization, String contactInfo);

    PatientResponseDTO addNewPatient(String firstName, String lastName, String gender, String nationalNumber, String username, String password, String contactInfo);

    int removeDoctor(long doctorId);

    boolean removePatient(long patientId);

    List<Doctor> getPatientDoctors(long id);


    void updatePatient(long patientId, String firstName, String lastName, String gender, String nationalNumber,String contactInfo);

    Stakeholder getStakeholderByUsername(String username);


    List<AvailabilityResponseDTO> getDoctorAvailablilities(long id);

    AvailabilityResponseDTO addDoctorAvailability(long id, LocalDateTime slot);

    AvailabilityResponseDTO getDoctorAvailability(int availabilityId);

    void removeAvailability(int availabilityId,long doctorId);


    AvailabilityResponseDTO getAvailability(int availabilityId);
}
