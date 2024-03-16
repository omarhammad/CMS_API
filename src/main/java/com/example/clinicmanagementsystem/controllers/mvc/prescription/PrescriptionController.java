package com.example.clinicmanagementsystem.controllers.mvc.prescription;

import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.domain.SessionHistory;
import com.example.clinicmanagementsystem.services.appointmentServices.IAppointmentService;
import com.example.clinicmanagementsystem.services.treatementServices.ITreatmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {


    ITreatmentService service;
    IAppointmentService appSvc;
    Logger logger;

    public PrescriptionController(ITreatmentService service, IAppointmentService appSvc) {
        this.service = service;
        this.appSvc = appSvc;
        logger = LoggerFactory.getLogger(PrescriptionController.class);
    }


    @GetMapping("/add/{appointmentId}")
    public String getAddPrescriptionsPage(Model model, HttpSession session, @PathVariable int appointmentId) {
        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Add Prescription Page"));

        List<Medication> medications = service.getAllMedications();
        model.addAttribute("medications", medications);
        model.addAttribute("prescriptionModel", new PrescriptionViewModel());
        model.addAttribute("appointmentId", appointmentId);

        return "prescriptions/add_prescription_page";
    }

    @PostMapping("/add/{appointmentId}")
    public String addNewPrescription(@Valid @ModelAttribute("prescriptionModel") PrescriptionViewModel prescriptionModel,
                                     BindingResult errors, Model model, @PathVariable int appointmentId) {
        List<Medication> medications = service.getAllMedications();
        if (prescriptionModel.getExpireDate() != null && appSvc.getAppointment(appointmentId).getAppointmentDateTime().toLocalDate().isAfter(prescriptionModel.getExpireDate())) {
            errors.addError(new FieldError("expireDate", "expireDate", "ExpireDate must be after the Appointment date and time!"));
        }
        if (errors.hasErrors()) {
            model.addAttribute("medications", medications);
            model.addAttribute("appointmentId", appointmentId);
            return "prescriptions/add_prescription_page";
        }

        List<Medication> selectedMedications = new ArrayList<>();
        for (int medicationId : prescriptionModel.getSelectedMedications()) {
            for (Medication medication : medications) {
                if (medication.getMedicationId() == medicationId) {
                    selectedMedications.add(medication);
                    break;
                }
            }
        }

        boolean saved = service.addNewPrescription(selectedMedications, prescriptionModel.getExpireDate(), appointmentId);
        if (saved) {
            logger.debug("Prescription added for appointmentId {}", appointmentId);
        } else {
            logger.debug("Prescription not added !");
        }
        return "redirect:/appointments/details/%d".formatted(appointmentId);
    }

    @GetMapping("/delete/{appointmentId}/{prescriptionId}")
    public String deletePrescription(@PathVariable int appointmentId, @PathVariable int prescriptionId) {
        boolean removed = service.removePrescription(prescriptionId);
        if (removed) {
            logger.debug("Prescription removed for appointmentId {}", appointmentId);
        } else {
            logger.debug("Prescription still exist !");
        }
        return "redirect:/appointments/details/%d".formatted(appointmentId);
    }

}
