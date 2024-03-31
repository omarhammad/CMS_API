package com.example.clinicmanagementsystem.controllers.mvc.patient;

import com.example.clinicmanagementsystem.domain.CustomUserDetails;
import com.example.clinicmanagementsystem.domain.UserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientsController {


    @GetMapping(value = {"/", ""})
    public String getPatientsPage() {
        return "patients/patients_page";
    }

    @GetMapping("/add")
    public String loadAddPatientPage() {
        return "patients/add_new_patient";
    }

    @GetMapping("/details/{id}")
    public String loadPatientDetailsPage(@PathVariable int id, Model model) {

        model.addAttribute("patient_id", id);
        return "patients/patient_details_page";
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String loadAuthedPatientPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long id = userDetails.getUserId();
        return "redirect:/patients/details/" + id;
    }


    @GetMapping("/update/{id}")
    public String loadUpdatePatientPage(@PathVariable int id) {
        return "patients/update_patient_page";
    }
}
