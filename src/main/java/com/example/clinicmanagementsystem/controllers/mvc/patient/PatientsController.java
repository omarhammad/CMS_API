package com.example.clinicmanagementsystem.controllers.mvc.patient;

import org.springframework.stereotype.Controller;
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
    public String loadPatientDetailsPage(@PathVariable int id) {
        return "patients/patient_details_page";
    }

    @GetMapping("/update/{id}")
    public String loadUpdatePatientPage(@PathVariable int id) {
        return "patients/update_patient_page";
    }
}
