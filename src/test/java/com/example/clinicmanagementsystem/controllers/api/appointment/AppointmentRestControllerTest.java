package com.example.clinicmanagementsystem.controllers.api.appointment;

import com.example.clinicmanagementsystem.dtos.appointments.CreateAppointmentRequestDTO;
import com.example.clinicmanagementsystem.services.appointmentServices.IAppointmentService;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentRestControllerTest {

    @Autowired
    private IAppointmentService appointmentService;

    @Autowired
    private IStakeholderService stakeholderService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CreateAppointmentRequestDTO> requestDTOList;

    @BeforeEach
    void setup() {

        stakeholderService.addNewPatient("Husam", "Albanna", "M", "97.11.16-633.38",
                "husam_albanna", "husam1997");
        stakeholderService.addNewPatient("Ahmad", "hammad", "M", "93.11.16-633.38",
                "ahmad_hammad", "ahmed_1993");

        stakeholderService.addNewPatient("Fadi", "Alaydi", "M", "98.11.16-633.38",
                "fadi", "fadi_1998");


        stakeholderService.addNewDoctor("Omar", "Hammad", "Urology", "omarhammad767@gmail.com,+32465358794",
                "omar_hammad", "omar1997");

        stakeholderService.addNewDoctor("Mahmoud", "Hammad", "Urology", "mahmoudha767@gmail.com,+32465358794",
                "mahmoud_hammad", "mahmoud1998");


    }

    @Test
    void addNewAppointment() throws Exception {

        // First,second and Last appointment goes well
        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(LocalDateTime.now().plusDays(3), "Eyes issue", 4, "93.11.16-633.38", "EMERGENCY"))))
                .andExpect(status().isCreated());
        CreateAppointmentRequestDTO secondAndThirdRequest = new CreateAppointmentRequestDTO(LocalDateTime.now().plusDays(3), "Eyes issue", 4, "97.11.16-633.38", "EMERGENCY");

        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondAndThirdRequest)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondAndThirdRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateAppointmentRequestDTO(LocalDateTime.now().plusDays(3), "Eyes issue", 5, "97.11.16-633.38", "EMERGENCY"))))
                .andExpect(status().isCreated());

    }

//    @Test
//    void getPatientAppointment() throws Exception {
//        mockMvc.perform(get("/api/appointments/patient/{id}", "1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
//                .andExpect(jsonPath("$[0].patient.firstName").value("Husam"));
//
//    }
//
//    @Test
//    void getDoctorAppointment() throws Exception {
//        mockMvc.perform(get("/api/appointments/doctor/{id}", "5")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
//                .andExpect(jsonPath("$[0].doctor.firstName").value("Mahmoud"));
//
//    }
//
//
//    @Test
//    void deleteAppointment() {
//    }

}
