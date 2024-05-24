package com.example.clinicmanagementsystem.controllers.api.appointment;

import com.example.clinicmanagementsystem.controllers.dtos.appointments.CreateAppointmentRequestDTO;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
// to disable the secConfigs temporarily add (addFilters = false) to AutoConfigureMockMvc
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
class AppointmentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IStakeholderService stakeholderService;

    @Autowired
    private ObjectMapper objectMapper;

    private int newDoctorId;
    private int newPatientId;

    @BeforeAll
    void setup() {
        newDoctorId = stakeholderService.addNewDoctor("Soso", "Hammad", "Urology", "sosohammad767@gmail.com,+32465358794", "sos_hammad", "omar1997").getId();
        newPatientId = stakeholderService.addNewPatient("Roro", "Hammad", "Urology", "87.03.15-321.96", "roro_hammad", "omar1997", "+32465358793,roro.hammad@email.com").getId();
    }


    @Test
    @WithUserDetails("admin")
    void newAppointmentAdded() throws Exception {
        mockMvc.perform(post("/api/appointments/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(6, "Eyes issue", 1, "91.07.23-543.21", "EMERGENCY"))))
                .andExpect(status().isCreated());
    }



    @Test
    @WithUserDetails("admin")
    void addAppointmentWithoutPatientNationalNumber() throws Exception {
        mockMvc.perform(post("/api/appointments/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(8, "Eyes issue", 2, null, "EMERGENCY"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.patientNN").exists());
    }

    @Test
    @WithUserDetails("admin")
    void addAppointmentWithPatientNationalNumberNotExists() throws Exception {
        mockMvc.perform(post("/api/appointments/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(8, "Eyes issue", 2, "91.07.23-543-22", "EMERGENCY"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.patientNN").exists());
    }


    @Test
    @WithUserDetails("admin")
    void addAppointmentWithoutAvailabiltyNotExsit() throws Exception {
        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(100, "Eyes issue", 5, "91.07.23-543-22", "EMERGENCY"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("admin")
    void addAppointmentWithInvalidDoctorId() throws Exception {
        mockMvc.perform(post("/api/appointments/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(8, "Eyes issue", -1, "91.07.23-543-22", "EMERGENCY"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.doctor").exists());
    }


    @Test
    @WithUserDetails("admin")
    void addAppointmentWithSlotNotForDoctor() throws Exception {
        mockMvc.perform(post("/api/appointments/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(8, "Eyes issue", 4, "91.07.23-543.21", "EMERGENCY"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exceptionMsg").exists())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

    }

    @Test
    @WithUserDetails("admin")
    void getPatientAppointment() throws Exception {
        mockMvc.perform(get("/api/appointments/patient/{id}", "8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(jsonPath("$[0].patient.firstName").value("Emma"));


        // New patient has no appointments yet "NO CONTENT"
        mockMvc.perform(get("/api/appointments/patient/{id}", newPatientId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // No patient in the system with id 20 "NOT FOUND"
        mockMvc.perform(get("/api/appointments/patient/{id}", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    @WithUserDetails("admin")
    void doctorHasNoAppointments() throws Exception {
        // New doctor has no appointments yet "NO CONTENT"
        mockMvc.perform(get("/api/appointments/doctor/{id}", newDoctorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("admin")
    void getAppointmentsForDoctorDoesNotExists() throws Exception {
        // No doctor in the system with id 20 "NOT FOUND"
        mockMvc.perform(get("/api/appointments/doctor/{id}", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithUserDetails("admin")
    void getDoctorAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/doctor/{id}", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(jsonPath("$[0].doctor.firstName").value("Omar"));
    }


    @Test
    @WithUserDetails("admin")
    void getDoctorHasNoAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/doctor/{id}", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("admin")
    void deleteAppointmentFound() throws Exception {
        mockMvc.perform(delete("/api/appointments/{id}", "1")
                        .with(csrf())
                )
                .andExpect(status().isNoContent());

    }

    @Test
    @WithUserDetails("admin")
    void deleteAppointmentNotFound() throws Exception {
        mockMvc.perform(delete("/api/appointments/{id}", "20")
                        .with(csrf())
                )

                .andExpect(status().isNotFound());
    }


}
