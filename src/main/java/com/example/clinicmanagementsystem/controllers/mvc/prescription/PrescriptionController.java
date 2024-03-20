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
    public String getAddPrescriptionsPage(@PathVariable long appointmentId, Model model) {
        model.addAttribute("medications", service.getAllMedications());
        return "prescriptions/add_prescription_page";
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
