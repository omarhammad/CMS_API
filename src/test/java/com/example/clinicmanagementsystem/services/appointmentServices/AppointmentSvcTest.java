package com.example.clinicmanagementsystem.services.appointmentServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppointmentSvcTest {


    @Autowired
    private AppointmentSvc appointmentSvc;

    @Autowired
    private StakeholdersSpringData stakeholdersRepo;
    @Autowired
    ModelMapper modelMapper;


    @Test
    void getAllAppointment() {
        List<AppointmentResponseDTO> responseDTOList = appointmentSvc.getAllAppointment();
        assertFalse(responseDTOList.isEmpty());
    }

    @Test
    void getAppointment() {

        AppointmentResponseDTO appointmentExists = appointmentSvc.getAppointment(1);
        AppointmentResponseDTO appointmentNotExists = appointmentSvc.getAppointment(20);
        assertNotNull(appointmentExists);
        assertNull(appointmentNotExists);
    }


    @Test
    @Transactional
    void addNewAppointment() {
        Appointment appointment = new Appointment(LocalDateTime.now().plusDays(3), "Bla Bla Bla",
                ((Doctor) stakeholdersRepo.findById(1L).orElse(null)),
                stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21"), AppointmentType.CONSULTATION);


        AppointmentResponseDTO appointmentResponseDTO = appointmentSvc.addNewAppointment(1, "91.07.23-543.21", appointment.getAppointmentDateTime(), appointment.getPurpose(), appointment.getAppointmentType());
        assertEquals(appointmentResponseDTO, modelMapper.map(appointment, AppointmentResponseDTO.class));


    }

    @Test
    @Transactional
    void updateAppointment() {

        AppointmentResponseDTO appointment = appointmentSvc.getAppointment(1);
        assertNotNull(appointment);

        String expectedPurpose = "Eyes burning";
        appointmentSvc.updateAppointment(appointment.getAppointmentId(), appointment.getDoctor().getId(), appointment.getPatient().getNationalNumber(),
                appointment.getAppointmentDateTime(), expectedPurpose, appointment.getAppointmentType());

        String actualPurpose = appointmentSvc.getAppointment(1).getPurpose();

        assertEquals(expectedPurpose, actualPurpose);

    }

    @Test
    @Transactional
    void removeAppointment() {
        AppointmentResponseDTO appointment = appointmentSvc.getAppointment(1);
        assertNotNull(appointment);

        appointmentSvc.removeAppointment(appointment.getAppointmentId());
        assertNull(appointmentSvc.getAppointment(appointment.getAppointmentId()));

    }

    @Test
    @Transactional
    void getPatientAppointments() {
        int patientId = 6;
        List<AppointmentResponseDTO> appointmentResponseDTOS =
                appointmentSvc.getPatientAppointments(patientId);
        for (AppointmentResponseDTO responseDTO : appointmentResponseDTOS) {
            assertEquals(patientId, responseDTO.getPatient().getId());
        }
    }

    @Test
    void getDoctorAppointments() {
        int doctorId = 6;
        List<AppointmentResponseDTO> appointmentResponseDTOS =
                appointmentSvc.getDoctorAppointments(doctorId);
        for (AppointmentResponseDTO responseDTO : appointmentResponseDTOS) {
            assertEquals(doctorId, responseDTO.getPatient().getId());
        }
    }
}