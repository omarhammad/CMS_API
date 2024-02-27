package com.example.clinicmanagementsystem.services.appointmentServices;


import com.example.clinicmanagementsystem.Exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AppointmentSvc implements IAppointmentService {


    AppointmentsSpringData appointmentRepo;
    StakeholdersSpringData stakeholdersRepo;


    public AppointmentSvc(AppointmentsSpringData appointmentRepo, StakeholdersSpringData stakeholdersRepo) {
        this.appointmentRepo = appointmentRepo;
        this.stakeholdersRepo = stakeholdersRepo;
    }

    @Override
    public List<Appointment> getAllAppointment() {
        return appointmentRepo.findAll();
    }

    @Override
    public List<Doctor> getDoctorsNames() {
        return appointmentRepo.findAllDoctorNames();
    }

    @Override
    public Appointment getAppointment(int appointmentId) {
        if (appointmentRepo.findById(appointmentId).isPresent()) {
            return appointmentRepo.findById(appointmentId).get();
        }
        return null;
    }

    @Override
    public boolean changeAppointmentDate(LocalDateTime newAppointmentDate) {
        return false;
    }

    @Override
    public boolean addNewAppointment(int doctorId, String patientNationalNumber, LocalDateTime appointmentDateTime, String purpose, AppointmentType type) {
        Appointment appointment = new Appointment();
        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber));

        if (appointment.getPatient() == null) throw new NationalNumberNotFoundException(patientNationalNumber);


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
        return true;
    }

    @Override
    public boolean removeAppointment(int appointmentId) {
        appointmentRepo.deleteById(appointmentId);
        return appointmentRepo.findById(appointmentId).isEmpty();
    }
}
