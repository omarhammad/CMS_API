package com.example.clinicmanagementsystem.services.appointmentServices;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Availability;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.exceptions.DoctorNotFoundException;
import com.example.clinicmanagementsystem.exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.exceptions.SlotUsedException;
import com.example.clinicmanagementsystem.exceptions.WrongSlotException;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.availabilitiesRepo.AvailabilitySpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentSvcTest {


    @Autowired
    private IAppointmentService appointmentSvc;

    @MockBean
    private StakeholdersSpringData stakeholdersRepo;

    @MockBean
    private AppointmentsSpringData appointmentsRepo;
    @Autowired
    ModelMapper modelMapper;
    @MockBean
    private AvailabilitySpringData availabilityRepo;


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

        Availability availability = new Availability();
        availability.setId(1L);
        availability.setUsed(false);
        availability.setDoctor(doctor);
        availability.setSlot(LocalDateTime.now().plusDays(10));
        given(availabilityRepo.findById(1)).willReturn(Optional.of(availability));

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setPurpose("BLA BLA BLA");
        appointment.setAvailabilitySlot(availability);
        appointment.setAppointmentType(AppointmentType.EMERGENCY);

        given(appointmentsRepo.save(any(Appointment.class))).willReturn(appointment);

        // WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentSvc.addNewAppointment(1, "91.07.23-543.21", 1, "BLA BLA BLA", AppointmentType.EMERGENCY);

        // THEN
        assertEquals(appointment.getAppointmentId(), appointmentResponseDTO.getAppointmentId());
        assertEquals(appointment.getDoctor().getId(), appointmentResponseDTO.getDoctor().getId());
        assertEquals(appointment.getPatient().getId(), appointmentResponseDTO.getPatient().getId());
        assertEquals(appointment.getPurpose(), appointmentResponseDTO.getPurpose());
        assertEquals(appointment.getAppointmentType(), appointmentResponseDTO.getAppointmentType());
        assertEquals(appointment.getAvailabilitySlot().getSlot(), appointmentResponseDTO.getAvailabilitySlot().getSlot());
    }


    @Test
    public void addAppointmentWithoutPatient() {

        // Given
        given(stakeholdersRepo.findById(1L)).willReturn(Optional.of(new Doctor()));
        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.empty());


        // When & Then

        assertThrows(NationalNumberNotFoundException.class,
                () -> appointmentSvc.addNewAppointment(1, "91.07.23-543.21",
                        1, "BLA BLA BLA", AppointmentType.EMERGENCY));


    }


    @Test
    public void addAppointmentWithoutDoctor() {


        // Given
        given(stakeholdersRepo.findPatientByNationalNumber("91.07.23-543.21")).willReturn(Optional.of(new Patient()));
        given(stakeholdersRepo.findById(anyLong())).willReturn(Optional.empty());


        // When & Then

        assertThrows(DoctorNotFoundException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21",
                1, "BLA BLA BLA", AppointmentType.EMERGENCY));

    }

    @Test
    public void addAppointmentAvailabilityNotExists() {

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

        given(availabilityRepo.findById(1)).willReturn(Optional.empty());


        //When & Then

        assertThrows(EntityNotFoundException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21",1
                , "BLA BLA BLA", AppointmentType.EMERGENCY));


    }


    @Test
    public void addAppointmentAvailabilityAlreadyUsed() {

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

        Availability availability = new Availability();
        availability.setId(1L);
        availability.setUsed(true);
        availability.setDoctor(doctor);
        availability.setSlot(LocalDateTime.now().plusDays(10));
        given(availabilityRepo.findById(1)).willReturn(Optional.of(availability));


        //When & Then

        assertThrows(SlotUsedException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21",1
                , "BLA BLA BLA", AppointmentType.EMERGENCY));


    }


    @Test
    public void addAppointmentAvailabilityNotForCurrentDoctor() {

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

        Availability availability = new Availability();
        availability.setId(1L);
        availability.setUsed(false);
        availability.setDoctor(new Doctor(2L));
        availability.setSlot(LocalDateTime.now().plusDays(10));
        given(availabilityRepo.findById(1)).willReturn(Optional.of(availability));


        //When & Then

        assertThrows(WrongSlotException.class, () -> appointmentSvc.addNewAppointment(1L, "91.07.23-543.21",1
                , "BLA BLA BLA", AppointmentType.EMERGENCY));


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


        Availability availability = new Availability();
        availability.setId(1L);
        availability.setUsed(false);
        availability.setDoctor(doctor);
        availability.setSlot(LocalDateTime.now().plusDays(10));
        given(availabilityRepo.findById(1)).willReturn(Optional.of(availability));


        Appointment expectedAppointment = new Appointment();
        expectedAppointment.setAppointmentId(1L);
        expectedAppointment.setDoctor(doctor);
        expectedAppointment.setPatient(patient);
        expectedAppointment.setAvailabilitySlot(availability);
        expectedAppointment.setPurpose("BLA BLA BLA");
        expectedAppointment.setAppointmentType(AppointmentType.EMERGENCY);
        given(appointmentsRepo.findById(1L)).willReturn(Optional.of(expectedAppointment));
        given(appointmentsRepo.save(any(Appointment.class))).willReturn(expectedAppointment);


        // WHEN
        AppointmentResponseDTO appointmentResponseDTO = appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21", 1, "BLA BLA BLA", AppointmentType.EMERGENCY);

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
                        1, "BLA BLA BLA", AppointmentType.EMERGENCY));


    }


    @Test
    public void updateAppointmentWithoutDoctor() {


        // Given
        given(appointmentsRepo.findById(anyLong())).willReturn(Optional.of(new Appointment()));
        given(stakeholdersRepo.findPatientByNationalNumber(anyString())).willReturn(Optional.of(new Patient()));
        given(stakeholdersRepo.findById(anyLong())).willReturn(Optional.empty());


        // When & Then

        assertThrows(DoctorNotFoundException.class, () -> appointmentSvc.updateAppointment(1L, 1L, "91.07.23-543.21",
                1, "BLA BLA BLA", AppointmentType.EMERGENCY));

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