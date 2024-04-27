package com.example.clinicmanagementsystem.services.appointmentServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.exceptions.DoctorNotFoundException;
import com.example.clinicmanagementsystem.exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AppointmentSvcTest {


    @Autowired
    private IAppointmentService appointmentSvc;

    @MockBean
    private StakeholdersSpringData stakeholdersRepo;

    @MockBean
    private AppointmentsSpringData appointmentsRepo;
    @Autowired
    ModelMapper modelMapper;


    @Test
    void getAllAppointment() {

        // Arrange/Given
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        appointments.add(new Appointment());

        given(appointmentsRepo.findAll()).willReturn(appointments);


        // Act / When
        List<AppointmentResponseDTO> responseDTOList = appointmentSvc.getAllAppointment();

        // Assert / Then
        assertEquals(3, responseDTOList.size());
    }

    @Test
    void getAppointmentExists() {

        //Arrange / Given
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);

        given(appointmentsRepo.findById(1L)).willReturn(Optional.of(appointment));

        // Act / When
        AppointmentResponseDTO appointmentExists = appointmentSvc.getAppointment(1);

        // Assert / Then
        assertEquals(1L, appointmentExists.getAppointmentId());

    }


    @Test
    void getAppointmentNotExists() {

        given(appointmentsRepo.findById(20L)).willReturn(Optional.empty());

        AppointmentResponseDTO appointmentNotExists = appointmentSvc.getAppointment(20L);

        assertNull(appointmentNotExists);
    }


    @Test
    void addNewAppointmentWhereDoesPatientAndDoctorExists() {

        // Given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Omar");
        doctor.setLastName("Hammad");
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(doctor));

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setNationalNumber("91.07.23-543.21");
        patient.setFirstName("Fadi");
        patient.setLastName("Alaydi");
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(patient));

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        appointment.setAppointmentDateTime(appointmentTime);
        appointment.setPurpose("BLA BLA BLA");
        appointment.setAppointmentType(AppointmentType.EMERGENCY);

        given(appointmentsRepo.save(any(Appointment.class))).willReturn(appointment);


        // WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentSvc.addNewAppointment(1, "91.07.23-543.21", appointmentTime, "BLA BLA BLA", AppointmentType.EMERGENCY);

        // THEN
        assertEquals(modelMapper.map(appointment, AppointmentResponseDTO.class), appointmentResponseDTO);


    }


    @Test
    public void addAppointmentWithoutPatient() {

        // Given
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(new Doctor()));
        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.empty());


        // When & Then

        assertThrows(NationalNumberNotFoundException.class,
                () -> appointmentSvc.addNewAppointment(1, "91.07.23-543.21",
                        LocalDateTime.now().plusDays(3), "BLA BLA BLA", AppointmentType.EMERGENCY));


    }


    @Test
    public void addAppointmentWithoutDoctor() {


        // Given
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(new Patient()));
        given(stakeholdersRepo.findById(anyLong())).willReturn(Optional.empty());


        // When & Then

        assertThrows(DoctorNotFoundException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21",
                LocalDateTime.now().plusDays(3), "BLA BLA BLA", AppointmentType.EMERGENCY));

    }


    @Test
    public void addAppointmentAlreadyExistsForSameDoctorAndPatient() {

        //Given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Omar");
        doctor.setLastName("Hammad");
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(doctor));

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setNationalNumber("91.07.23-543.21");
        patient.setFirstName("Fadi");
        patient.setLastName("Alaydi");
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(patient));
        given(appointmentsRepo.save(any(Appointment.class))).willThrow(new DataIntegrityViolationException("appointments_appointment_date_time_doctor_id_patient_id_key"));

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        //When & Then

        InvalidAppointmentException thrown = assertThrows(InvalidAppointmentException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21", appointmentTime
                , "BLA BLA BLA", AppointmentType.EMERGENCY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
        String appDateTime = appointmentTime.format(formatter);
        String expectedMessage = "Invalid appointment because,  Dr.%s and Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patient.getFirstName() + " " + patient.getLastName(), doctor.getFirstName() + " " + doctor.getLastName(), appDateTime);

        assertEquals(expectedMessage, thrown.getMessage());

    }


    @Test
    public void addAppointmentForSlotUsedByTheSameDoctor() {

        //Given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Omar");
        doctor.setLastName("Hammad");
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(doctor));

        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.of(new Patient()));
        given(appointmentsRepo.save(any(Appointment.class))).willThrow(new DataIntegrityViolationException("appointments_appointment_date_time_doctor_id_key"));

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        //When & Then

        InvalidAppointmentException thrown = assertThrows(InvalidAppointmentException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21", appointmentTime
                , "BLA BLA BLA", AppointmentType.EMERGENCY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
        String appDateTime = appointmentTime.format(formatter);
        String expectedMessage = "Invalid appointment because,  Dr.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(doctor.getFirstName() + ' ' + doctor.getLastName(), appDateTime);


        assertEquals(expectedMessage, thrown.getMessage());

    }

    @Test
    public void addAppointmentForSlotUsedByTheSamePatient() {

        //Given
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(new Doctor()));

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setNationalNumber("91.07.23-543.21");
        patient.setFirstName("Fadi");
        patient.setLastName("Alaydi");
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(patient));
        given(appointmentsRepo.save(any(Appointment.class))).willThrow(new DataIntegrityViolationException("appointments_appointment_date_time_patient_id_key"));

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        //When & Then

        InvalidAppointmentException thrown = assertThrows(InvalidAppointmentException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21", appointmentTime
                , "BLA BLA BLA", AppointmentType.EMERGENCY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
        String appDateTime = appointmentTime.format(formatter);
        String expectedMessage = "Invalid appointment because,  Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patient.getFirstName() + ' ' + patient.getLastName(), appDateTime);


        assertEquals(expectedMessage, thrown.getMessage());

    }

    @Test
    void updateNewAppointmentWhereDoesPatientAndDoctorExists() {

        // Given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Omar");
        doctor.setLastName("Hammad");
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(doctor));

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setNationalNumber("91.07.23-543.21");
        patient.setFirstName("Fadi");
        patient.setLastName("Alaydi");
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(patient));

        Appointment expectedAppointment = new Appointment();
        expectedAppointment.setAppointmentId(1L);
        expectedAppointment.setDoctor(doctor);
        expectedAppointment.setPatient(patient);
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        expectedAppointment.setAppointmentDateTime(appointmentTime);
        expectedAppointment.setPurpose("BLA BLA BLA");
        expectedAppointment.setAppointmentType(AppointmentType.EMERGENCY);
        given(appointmentsRepo.findById(1L)).willReturn(Optional.of(expectedAppointment));
        given(appointmentsRepo.save(any(Appointment.class))).willReturn(expectedAppointment);


        // WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21", appointmentTime, "BLA BLA BLA", AppointmentType.EMERGENCY);

        // THEN
        assertEquals(modelMapper.map(expectedAppointment, AppointmentResponseDTO.class), appointmentResponseDTO);


    }


    @Test
    public void updateAppointmentWithoutPatient() {

        // Given
        given(appointmentsRepo.findById(anyLong())).willReturn(Optional.of(new Appointment()));
        given(stakeholdersRepo.findById(anyLong())).willReturn(Optional.of(new Doctor()));
        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.empty());


        // When & Then

        assertThrows(NationalNumberNotFoundException.class,
                () -> appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21",
                        LocalDateTime.now().plusDays(3), "BLA BLA BLA", AppointmentType.EMERGENCY));


    }


    @Test
    public void updateAppointmentWithoutDoctor() {


        // Given
        given(appointmentsRepo.findById(anyLong())).willReturn(Optional.of(new Appointment()));
        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.of(new Patient()));
        given(stakeholdersRepo.findById(anyLong())).willReturn(Optional.empty());


        // When & Then

        assertThrows(DoctorNotFoundException.class, () -> appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21",
                LocalDateTime.now().plusDays(3), "BLA BLA BLA", AppointmentType.EMERGENCY));

    }


    @Test
    public void updateAppointmentAlreadyExistsForSameDoctorAndPatient() {

        //Given
        given(appointmentsRepo.findById(1L)).willReturn(Optional.of(new Appointment()));

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Omar");
        doctor.setLastName("Hammad");
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(doctor));

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setNationalNumber("91.07.23-543.21");
        patient.setFirstName("Fadi");
        patient.setLastName("Alaydi");
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(patient));
        given(appointmentsRepo.save(any(Appointment.class))).willThrow(new DataIntegrityViolationException("appointments_appointment_date_time_doctor_id_patient_id_key"));

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        //When & Then

        InvalidAppointmentException thrown = assertThrows(InvalidAppointmentException.class, () -> appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21", appointmentTime
                , "BLA BLA BLA", AppointmentType.EMERGENCY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
        String appDateTime = appointmentTime.format(formatter);
        String expectedMessage = "Invalid appointment because,  Dr.%s and Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patient.getFirstName() + " " + patient.getLastName(), doctor.getFirstName() + " " + doctor.getLastName(), appDateTime);

        assertEquals(expectedMessage, thrown.getMessage());

    }


    @Test
    public void updateAppointmentForSlotUsedByTheSameDoctor() {

        //Given
        given(appointmentsRepo.findById(anyLong())).willReturn(Optional.of(new Appointment()));
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Omar");
        doctor.setLastName("Hammad");
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(doctor));

        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.of(new Patient()));
        given(appointmentsRepo.save(any(Appointment.class))).willThrow(new DataIntegrityViolationException("appointments_appointment_date_time_doctor_id_key"));

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        //When & Then

        InvalidAppointmentException thrown = assertThrows(InvalidAppointmentException.class, () -> appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21", appointmentTime
                , "BLA BLA BLA", AppointmentType.EMERGENCY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
        String appDateTime = appointmentTime.format(formatter);
        String expectedMessage = "Invalid appointment because,  Dr.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(doctor.getFirstName() + ' ' + doctor.getLastName(), appDateTime);


        assertEquals(expectedMessage, thrown.getMessage());

    }

    @Test
    public void updateAppointmentForSlotUsedByTheSamePatient() {

        //Given
        given(appointmentsRepo.findById(anyLong())).willReturn(Optional.of(new Appointment()));
        given(stakeholdersRepo.findById(anyLong())).willReturn(Optional.of(new Doctor()));

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setNationalNumber("91.07.23-543.21");
        patient.setFirstName("Fadi");
        patient.setLastName("Alaydi");
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(patient));
        given(appointmentsRepo.save(any(Appointment.class))).willThrow(new DataIntegrityViolationException("appointments_appointment_date_time_patient_id_key"));

        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(3);
        //When & Then

        InvalidAppointmentException thrown = assertThrows(InvalidAppointmentException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21", appointmentTime
                , "BLA BLA BLA", AppointmentType.EMERGENCY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM hh:mm a");
        String appDateTime = appointmentTime.format(formatter);
        String expectedMessage = "Invalid appointment because,  Pt.%s have the same appointment at [ %s ] , Choose another slot! ".formatted(patient.getFirstName() + ' ' + patient.getLastName(), appDateTime);


        assertEquals(expectedMessage, thrown.getMessage());

    }

    @Test
    void removeAppointmentThatExists() {

        // GIVEN
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        given(appointmentsRepo.findById(1L)).willReturn(Optional.of(appointment));
        doNothing().when(appointmentsRepo).deleteById(1L);

        // WHEN
        boolean isRemoved = appointmentSvc.removeAppointment(1L);

        // THEN
        assertTrue(isRemoved, "appointment is not removed ");
        verify(appointmentsRepo).deleteById(1L);

    }

    @Test
    void removeAppointmentThatDoesExists() {

        // GIVEN
        given(appointmentsRepo.findById(1L)).willReturn(Optional.empty());

        // WHEN
        boolean isNotRemoved = appointmentSvc.removeAppointment(1L);

        // THEN
        assertFalse(isNotRemoved, "appointment does not exist");

    }


    @Test
    void getPatientAppointments() {
        // GIVEN
        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        given(appointmentsRepo.getAppointmentByPatientId(2L)).willReturn(appointments);
        // WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOS = appointmentSvc.getPatientAppointments(2L);
        // THEN
        assertEquals(appointments.size(), appointmentResponseDTOS.size());
    }

    @Test
    void getDoctorAppointments() {
        // Given
        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        given(appointmentsRepo.findAppointmentByDoctor_Id(1L)).willReturn(appointments);
        // WHEN
        List<AppointmentResponseDTO> appointmentResponseDTOS = appointmentSvc.getDoctorAppointments(1L);

        // THEN
        assertEquals(appointments.size(), appointmentResponseDTOS.size());
    }
}