package com.example.clinicmanagementsystem.controllers.mvc.medications;

import com.example.clinicmanagementsystem.Exceptions.MedicationDeletionException;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.example.clinicmanagementsystem.domain.SessionHistory;
import com.example.clinicmanagementsystem.domain.util.Unit;
import com.example.clinicmanagementsystem.services.treatementServices.ITreatmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/medications")
public class MedicationsController {

    @GetMapping({"/", ""})
    public String getMedicationsPage() {
        return "/medications/medications_page";
    }


    @GetMapping("/add")
    public String getAddMedicationPage(Model model) {
        model.addAttribute("units", Unit.values());
        model.addAttribute("medicationForms", MedicationForm.values());
        return "/medications/add_medications_page";
    }


    @GetMapping("/update/{id}")
    public String getUpdateMedicationPage(Model model, @PathVariable int id) {
        model.addAttribute("units", Unit.values());
        model.addAttribute("medicationForms", MedicationForm.values());
        return "/medications/update_medications_page";
    }


    @GetMapping("/details/{id}")
    public String getMedicationDetailsPage(@PathVariable int id) {
        return "/medications/medication_details_page";
    }
}
