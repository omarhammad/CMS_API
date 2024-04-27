package com.example.clinicmanagementsystem.services.appointmentServices;


import com.example.clinicmanagementsystem.exceptions.AppointmentNotFoundException;
import com.example.clinicmanagementsystem.exceptions.DoctorNotFoundException;
import com.example.clinicmanagementsystem.exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentSvc implements IAppointmentService {


    private final AppointmentsSpringData appointmentRepo;
    private final StakeholdersSpringData stakeholdersRepo;
    private final ModelMapper modelMapper;


    public AppointmentSvc(AppointmentsSpringData appointmentRepo, StakeholdersSpringData stakeholdersRepo, ModelMapper modelMapper) {
        this.appointmentRepo = appointmentRepo;
        this.stakeholdersRepo = stakeholdersRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointment() {
        List<Appointment> appointments = appointmentRepo.findAll();
        List<AppointmentResponseDTO> appointmentResponseDTOS = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentResponseDTOS.add(modelMapper.map(appointment, AppointmentResponseDTO.class));
        }


        return appointmentResponseDTOS;
    }

    @Override
    public List<Doctor> getDoctorsNames() {
        return appointmentRepo.findAllDoctorNames();
    }

    @Override
    public AppointmentResponseDTO getAppointment(long appointmentId) {
        if (appointmentRepo.findById(appointmentId).isPresent()) {
            return modelMapper.map(appointmentRepo.findById(appointmentId).get(), AppointmentResponseDTO.class);
        }
        return null;
    }

    @Override
    public boolean changeAppointmentDate(LocalDateTime newAppointmentDate) {
        return false;
    }

    @Override
    public AppointmentResponseDTO addNewAppointment(long doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type) {
        Appointment appointment = new Appointment();
        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber).orElse(null));

        if (appointment.getPatient() == null) throw new NationalNumberNotFoundException(patientNationalNumber);
        if (appointment.getDoctor() == null) throw new DoctorNotFoundException("Doctor not found!");


        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setPurpose(purpose);
        appointment.setAppointmentType(type);

        Appointment savedAppointment;
        try {
            savedAppointment = appointmentRepo.save(appointment);
        } catch (DataIntegrityViolationException e) {
            String message = "";
            String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
            String patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
            String appDateTime = appointment.getAppointmentDateTime().format(formatter);

            if (e.getMessage().contains("appointments_appointment_date_time_doctor_id_patient_id_key")) {
                System.out.println("doctor and patient has the same appointment slot");
                message = "Invalid appointment because,  Dr.%s and Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patientName, doctorName, appDateTime);

            } else if (e.getMessage().contains("appointments_appointment_date_time_patient_id_key")) {
                System.out.println("patient has the same appointment slot");
                message = "Invalid appointment because,  Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patientName, appDateTime);

            } else if (e.getMessage().contains("appointments_appointment_date_time_doctor_id_key")) {
                System.out.println("doctor has the same appointment slot");
                message = "Invalid appointment because,  Dr.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(doctorName, appDateTime);

            }

            throw new InvalidAppointmentException(message);
        }

        return modelMapper.map(savedAppointment, AppointmentResponseDTO.class);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(long appointmentId, long doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type) {

        if (appointmentRepo.findById(appointmentId).isEmpty())
            throw new AppointmentNotFoundException("Appointment Not Found!");


        Appointment appointment = new Appointment();
        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber).orElse(null));

        if (appointment.getPatient() == null) throw new NationalNumberNotFoundException(patientNationalNumber);
        if (appointment.getDoctor() == null) throw new DoctorNotFoundException("Doctor Not Found!");


        appointment.setAppointmentId(appointmentId);
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setPurpose(purpose);
        appointment.setAppointmentType(type);

        try {
            return modelMapper.map(appointmentRepo.save(appointment), AppointmentResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            String message = "";
            String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
            String patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
            String appDateTime = appointment.getAppointmentDateTime().format(formatter);

            if (e.getMessage().contains("appointments_appointment_date_time_doctor_id_patient_id_key")) {
                System.out.println("doctor and patient has the same appointment slot");
                message = "Invalid appointment because,  Dr.%s and Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patientName, doctorName, appDateTime);

            } else if (e.getMessage().contains("appointments_appointment_date_time_patient_id_key")) {
                System.out.println("patient has the same appointment slot");
                message = "Invalid appointment because,  Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patientName, appDateTime);

            } else if (e.getMessage().contains("appointments_appointment_date_time_doctor_id_key")) {
                System.out.println("doctor has the same appointment slot");
                message = "Invalid appointment because,  Dr.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(doctorName, appDateTime);

            }

            throw new InvalidAppointmentException(message);
        }

    }

    @Override
    public boolean removeAppointment(long appointmentId) {
        if (appointmentRepo.findById(appointmentId).isPresent()) {
            appointmentRepo.deleteById(appointmentId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<AppointmentResponseDTO> getPatientAppointments(long id) {
        List<Appointment> patientAppointments = appointmentRepo.getAppointmentByPatientId(id);
        System.out.println(patientAppointments);
        List<AppointmentResponseDTO> responseDTOS = new ArrayList<>();
        for (Appointment appointment : patientAppointments) {
            responseDTOS.add(modelMapper.map(appointment, AppointmentResponseDTO.class));
        }
        return responseDTOS;
    }

    @Override
    public List<AppointmentResponseDTO> getDoctorAppointments(long id) {
        List<Appointment> patientAppointments = appointmentRepo.findAppointmentByDoctor_Id(id);
        List<AppointmentResponseDTO> responseDTOS = new ArrayList<>();
        for (Appointment appointment : patientAppointments) {
            responseDTOS.add(modelMapper.map(appointment, AppointmentResponseDTO.class));
        }
        return responseDTOS;
    }
}
