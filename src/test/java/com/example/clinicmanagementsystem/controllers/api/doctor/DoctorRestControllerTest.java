package com.example.clinicmanagementsystem.controllers.api.doctor;

import com.example.clinicmanagementsystem.controllers.dtos.doctors.CreateAvailabilityRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DoctorRestControllerTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithUserDetails("sara_lee")
    void getDoctorAvailabilities() throws Exception {

        mockMvc.perform(get("/api/doctors/{id}/availability", 2))
                .andDo(print())
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    boolean isStatusValid = (status == 200 || status == 204);
                    assertTrue("Expected HTTP status 200 or 204, but got " + status, isStatusValid);
                });
    }

    @Test
    @WithUserDetails("sara_lee")
    void addDoctorAvailability() throws Exception {
        CreateAvailabilityRequestDTO requestDTO = new CreateAvailabilityRequestDTO(LocalDateTime.now().plusDays(3));

        mockMvc.perform(post("/api/doctors/{id}/availability", 2)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @WithUserDetails("nora_davis")
    void deleteDoctorAvailabilityByIdThatIsUsed() throws Exception {

        mockMvc.perform(delete("/api/doctors/availability/{availabilityId}", 4).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("sara_lee")
    void deleteDoctorAvailabilityNotExists() throws Exception {

        mockMvc.perform(delete("/api/doctors/availability/{availabilityId}", 100).with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("sara_lee")
    void deleteDoctorAvailability() throws Exception {

        mockMvc.perform(delete("/api/doctors/availability/{availabilityId}", 7).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("sara_lee")
    void deleteDoctorAvailabilityNotForHer() throws Exception {

        mockMvc.perform(delete("/api/doctors/availability/{availabilityId}", 1).with(csrf()))
                .andExpect(status().isForbidden());
    }
}