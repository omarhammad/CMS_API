package com.example.clinicmanagementsystem.services.appointmentServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.dtos.appointments.AppointmentResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {


    List<AppointmentResponseDTO> getAllAppointment();


    List<Doctor> getDoctorsNames();

    AppointmentResponseDTO getAppointment(long appointmentId);

    boolean changeAppointmentDate(LocalDateTime newAppointmentDate);

    AppointmentResponseDTO addNewAppointment(long doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type);

    void updateAppointment(long appointmentId, long doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type);


    boolean removeAppointment(long appointmentId);

    List<AppointmentResponseDTO> getPatientAppointments(int id);
    List<AppointmentResponseDTO> getDoctorAppointments(long id);
}
