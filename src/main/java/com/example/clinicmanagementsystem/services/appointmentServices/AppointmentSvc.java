package com.example.clinicmanagementsystem.services.appointmentServices;


import com.example.clinicmanagementsystem.Exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.dtos.appointments.AppointmentResponseDTO;
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
    public AppointmentResponseDTO addNewAppointment(int doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type) {
        Appointment appointment = new Appointment();
        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber));

        if (appointment.getPatient() == null) throw new NationalNumberNotFoundException(patientNationalNumber);


        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setPurpose(purpose);
        appointment.setAppointmentType(type);

        Appointment savedAppointment;
        try {
            savedAppointment = appointmentRepo.save(appointment);
        } catch (DataIntegrityViolationException e) {
            String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
            String patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
            String appDateTime = appointment.getAppointmentDateTime().format(formatter);
            throw new InvalidAppointmentException(patientName, doctorName, appDateTime);
        }
        return modelMapper.map(savedAppointment, AppointmentResponseDTO.class);
    }

    @Override
    public void updateAppointment(long appointmentId, int doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type) {
        Appointment appointment = new Appointment();
        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber));

        if (appointment.getPatient() == null) throw new NationalNumberNotFoundException(patientNationalNumber);


        appointment.setAppointmentId(appointmentId);
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setPurpose(purpose);
        appointment.setAppointmentType(type);

        try {
            appointmentRepo.save(appointment);
        } catch (DataIntegrityViolationException e) {
            String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
            String patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
            String appDateTime = appointment.getAppointmentDateTime().format(formatter);
            throw new InvalidAppointmentException(patientName, doctorName, appDateTime);
        }

    }

    @Override
    public boolean removeAppointment(long appointmentId) {
        appointmentRepo.deleteById(appointmentId);
        return appointmentRepo.findById(appointmentId).isEmpty();
    }
}
