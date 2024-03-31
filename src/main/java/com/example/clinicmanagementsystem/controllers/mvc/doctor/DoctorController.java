package com.example.clinicmanagementsystem.controllers.mvc.doctor;

import com.example.clinicmanagementsystem.domain.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctors")
public class DoctorController {


    @GetMapping({"/", ""})
    public String loadDoctorsPage() {
        return "doctors/doctors_page";
    }

    @GetMapping("/add")
    public String loadAddDoctorPage() {
        return "doctors/add_new_doctor";
    }

    @GetMapping("/update/{id}")
    public String loadUpdatePage(@PathVariable long id) {
        return "doctors/update_doctor_page";
    }

    @GetMapping("/details/{id}")
    public String loadDoctorDetailsPage(@PathVariable long id) {
        return "doctors/doctor_details_page";
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public String loadAuthedDoctorPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long id = userDetails.getUserId();
        return "redirect:/doctors/details/" + id;
    }

    @GetMapping("/update")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public String loadAuthedUpdateDoctorPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long id = userDetails.getUserId();
        return "redirect:/doctors/update/" + id;
    }
}
