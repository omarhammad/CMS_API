package com.example.clinicmanagementsystem.presentation.PatientDetails;

import com.example.clinicmanagementsystem.Exceptions.NationalNumberExistException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.SessionHistory;
import com.example.clinicmanagementsystem.services.stakeholdersServices.StakeholderService;
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
@RequestMapping("/patients")
public class PatientDetailsController {


    private final StakeholderService service;
    private final Logger logger;

    @Autowired
    public PatientDetailsController(StakeholderService service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(PatientDetailsController.class);
    }

    @GetMapping(value = {"/", ""})
    public String getPatientsPage(Model model, HttpSession session) {
        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Patients Page"));
        logger.debug("Session History {}", sessionHistories);

        logger.debug("Loading Patients Page....");
        List<Patient> patients = service.getAllPatients();
        model.addAttribute("patients", patients);
        return "patients/patients_page";
    }

    @GetMapping("/add")
    public String getAddPatientPage(Model model, HttpSession session) {

        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Add Patient Page"));
        logger.debug("Session History {}", sessionHistories);


        logger.debug("Loading Add Patient Page... ");
        model.addAttribute("patientModel", new PatientViewModel());
        return "patients/add_new_patient";
    }

    @PostMapping("/add")
    public String addPatient(@Valid @ModelAttribute("patientModel") PatientViewModel patientModel, BindingResult errors) {
        if (errors.hasErrors()) {
            return "patients/add_new_patient";
        }
        logger.debug("Adding new Patient : {}", patientModel);
        boolean saved = service.addNewPatient(patientModel.getFirstName(), patientModel.getLastName(), patientModel.getGender(), patientModel.getNationalNumber());
        if (saved) {
            logger.debug("Saved!");
        } else {
            logger.debug("Not saved");
        }
        return "redirect:/patients/";
    }

    @GetMapping("/details/{id}")
    public String getAppointmentDetailsPage(Model model, @PathVariable int id) {
        Patient patient = service.getAPatient(id);
        List<Doctor> doctors = service.getPatientDoctors(id);
        List<Appointment> lastAppointments = service.getPatientOldAppointments(id);
        logger.debug("Old appointments are : {}", lastAppointments);

        model.addAttribute("patient", patient);
        model.addAttribute("hisDoctors", doctors);
        model.addAttribute("lastAppointments", lastAppointments);
        return "patients/patient_details_page";
    }


    @GetMapping("/delete/{id}")
    public String removePatient(@PathVariable int id) {
        if (service.removePatient(id)) {
            logger.debug("Appointment Deleted!");
        } else {
            logger.debug("Appointment still saved!");

        }
        return "redirect:/patients/";
    }


    @ExceptionHandler(NationalNumberExistException.class)
    public String handleNationalNumberNotFoundException(NationalNumberExistException e, RedirectAttributes redirectAttributes) {
        logger.debug("NationalNumberNotFoundException: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("exceptionMsg", e.getMessage());
        return "redirect:/patients/add";
    }
}
