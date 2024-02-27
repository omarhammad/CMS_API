package com.example.clinicmanagementsystem.controllers.medcations;

import com.example.clinicmanagementsystem.Exceptions.MedicationDeletionException;
import com.example.clinicmanagementsystem.domain.Medication;
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
import java.util.List;

@Controller
@RequestMapping("/medications")
public class MedicationsController {

    private final ITreatmentService service;
    private Logger logger;

    @Autowired
    public MedicationsController(ITreatmentService service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(MedicationsController.class);
    }

    @GetMapping({"/", ""})
    public String getMedicationsPage(Model model, HttpSession session) {
        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Medications Page"));
        logger.debug("Session History {}", sessionHistories);

        List<Medication> medications = service.getAllMedications();
        logger.debug("Load Medications Page {}", medications);
        model.addAttribute("medications", medications);
        return "/medications/medications_page";
    }


    @GetMapping("/add")
    public String getAddMedicationPage(Model model, HttpSession session) {

        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Add Medication Page"));
        logger.debug("Session History {}", sessionHistories);


        model.addAttribute("medicationModel", new MedicationViewModel());
        model.addAttribute("units", Unit.values());
        model.addAttribute("medicationForms", MedicationForm.values());
        return "/medications/add_medications_page";
    }


    @PostMapping("/add")
    public String addMedication(Model model, @Valid @ModelAttribute("medicationModel") MedicationViewModel viewModel
            , BindingResult errors) {
        if (errors.hasErrors()) {
            model.addAttribute("medicationForms", MedicationForm.values());
            model.addAttribute("units", Unit.values());
            return "medications/add_medications_page";
        }

        boolean saved = service.addNewMedication(viewModel.getName(), viewModel.getForm(), viewModel.getQuantity(),
                viewModel.getUnit(), viewModel.getFrequencies(), viewModel.getDaysDuration(), viewModel.getNotes());
        if (saved) {
            logger.debug("Medication Saved : {}", viewModel);
        } else {
            logger.debug("Medication is not saved !");
        }
        return "redirect:/medications/";
    }


    @GetMapping("/details/{id}")
    public String getMedicationDetailsPage(@PathVariable int id, Model model) {
        Medication medication = service.getMedication(id);
        model.addAttribute("medication", medication);
        return "/medications/medication_details_page";
    }


    @GetMapping("/delete/{id}")
    public String deleteMedication(@PathVariable int id) {
        if (service.removeMedication(id)) {
            logger.debug("Medication Deleted!");
        } else {
            logger.debug("Medication still saved!");
        }
        return "redirect:/medications/";
    }

    @ExceptionHandler(MedicationDeletionException.class)
    public String handleMedicationDeletionException(MedicationDeletionException e, Model model, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("exceptionMsg", e.getMessage());
        return "redirect:/medications/";
    }
}
