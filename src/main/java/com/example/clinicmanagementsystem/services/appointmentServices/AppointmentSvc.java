package com.example.clinicmanagementsystem.services.appointmentServices;


import com.example.clinicmanagementsystem.domain.Availability;
import com.example.clinicmanagementsystem.exceptions.*;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.availabilitiesRepo.AvailabilitySpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final AvailabilitySpringData availabilityRepo;
    private final ModelMapper modelMapper;


    public AppointmentSvc(AppointmentsSpringData appointmentRepo, StakeholdersSpringData stakeholdersRepo, AvailabilitySpringData availabilityRepo, ModelMapper modelMapper) {
        this.appointmentRepo = appointmentRepo;
        this.stakeholdersRepo = stakeholdersRepo;
        this.availabilityRepo = availabilityRepo;
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
    @Transactional
    public AppointmentResponseDTO addNewAppointment(long doctorId, String patientNationalNumber, int appointmentSlotId, String purpose, AppointmentType type) {
        Appointment appointment = new Appointment();
        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber).orElse(null));

        if (appointment.getPatient() == null) throw new NationalNumberNotFoundException(patientNationalNumber);
        if (appointment.getDoctor() == null) throw new DoctorNotFoundException("Doctor not found!");

        Availability availability = availabilityRepo.findById(appointmentSlotId).orElseThrow(() -> new EntityNotFoundException("Availability Not Found!"));

        if (availability.isUsed()) {
            throw new SlotUsedException("This slot is used!");
        }

        if (availability.getDoctor().getId() != doctorId){
            throw new WrongSlotException("This slot does not belong to this doctor!");
        }

            appointment.setAvailabilitySlot(availability);
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
            String appDateTime = appointment.getAvailabilitySlot().getSlot().format(formatter);

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

        availability.setUsed(true);
        availabilityRepo.save(availability);

        return modelMapper.map(savedAppointment, AppointmentResponseDTO.class);
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointment(long appointmentId, long doctorId, String patientNationalNumber, int availabilitySlotId, String purpose, AppointmentType type) {

        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()
                -> new AppointmentNotFoundException("Appointment Not Found!"));

        Availability oldAvailability = appointment.getAvailabilitySlot();

        appointment.setDoctor((Doctor) stakeholdersRepo.findById(doctorId).orElseThrow(() ->
                new DoctorNotFoundException("Doctor Not Found!")));

        appointment.setPatient(stakeholdersRepo.findPatientByNationalNumber(patientNationalNumber).orElseThrow(() ->
                new NationalNumberNotFoundException(patientNationalNumber)));

        Availability newAvailability = availabilityRepo.findById(availabilitySlotId).orElseThrow(() ->
                new EntityNotFoundException("Availability Not Found!"));


        if (!oldAvailability.getId().equals(newAvailability.getId())) {

            if (newAvailability.isUsed()) {
                throw new SlotUsedException("Slot already Used!");
            }
            oldAvailability.setUsed(false);
            newAvailability.setUsed(true);

            availabilityRepo.save(oldAvailability);
            availabilityRepo.save(newAvailability);

        }

        appointment.setAppointmentId(appointmentId);
        appointment.setAvailabilitySlot(newAvailability);
        appointment.setPurpose(purpose);
        appointment.setAppointmentType(type);

        try {
            return modelMapper.map(appointmentRepo.save(appointment), AppointmentResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            String message = "";
            String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
            String patientName = appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
            String appDateTime = appointment.getAvailabilitySlot().getSlot().format(formatter);

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
    @Transactional
    public boolean removeAppointment(long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment Not Found!"));
        Availability availability = appointment.getAvailabilitySlot();
        availability.setUsed(false);
        availabilityRepo.save(availability);
        appointmentRepo.deleteById(appointmentId);
        return true;

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
