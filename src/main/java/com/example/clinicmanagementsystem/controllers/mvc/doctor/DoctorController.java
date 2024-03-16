package com.example.clinicmanagementsystem.controllers.mvc.doctor;

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

}
