package com.example.clinicmanagementsystem.services.appointmentServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.domain.Doctor;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {


    List<Appointment> getAllAppointment();


    List<Doctor> getDoctorsNames();

    Appointment getAppointment(int appointmentId);

    boolean changeAppointmentDate(LocalDateTime newAppointmentDate);

    boolean addNewAppointment(int doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type);

    boolean removeAppointment(int appointmentId);

}
