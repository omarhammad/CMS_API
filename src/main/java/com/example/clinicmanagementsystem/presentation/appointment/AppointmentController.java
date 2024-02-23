package com.example.clinicmanagementsystem.presentation.appointment;

import com.example.clinicmanagementsystem.Exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.domain.SessionHistory;
import com.example.clinicmanagementsystem.services.appointmentServices.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final Logger logger;
    AppointmentService service;

    @Autowired
    public AppointmentController(AppointmentService service) {
        logger = LoggerFactory.getLogger(AppointmentController.class);
        this.service = service;
    }

    @GetMapping(value = {"/", ""})
    public String showAppointmentsPage(Model model, HttpSession session) {
        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Appointments Page"));
        logger.debug("Session History {}", sessionHistories);

        logger.debug("Loading Appointments Page.....");
        List<Appointment> appointments = service.getAllAppointment();
        model.addAttribute("appointments", appointments);
        return "appointments/appointments_page";
    }


    @GetMapping("/add")
    public String getAddAppointmetnPage(Model model, HttpSession session) {

        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Add Appointments Page"));
        logger.debug("Session History {}", sessionHistories);

        logger.debug("Loading Add Appointments Form.....");
        model.addAttribute("doctors", service.getDoctorsNames());
        model.addAttribute("appointmentTypes", AppointmentType.values());
        model.addAttribute("appointmentModel", new AppointmentViewModel());
        return "appointments/add-appointment";
    }

    @PostMapping("/add")
    public String addAppointment(Model model, @Valid @ModelAttribute("appointmentModel") AppointmentViewModel appointmentModel,
                                 BindingResult errors) {
        logger.debug("Save New Appointment.....{}", appointmentModel);
        logger.debug("Errors : {}", errors);
        if (errors.hasErrors()) {
            model.addAttribute("doctors", service.getDoctorsNames());
            model.addAttribute("appointmentTypes", AppointmentType.values());
            return "appointments/add-appointment";
        }

        boolean saved;

        saved = service.addNewAppointment(appointmentModel.getDoctorId(), appointmentModel.getPatientNationalNumber(),
                appointmentModel.getAppointmentDateTime(),
                appointmentModel.getPurpose(), appointmentModel.getAppointmentType());

        if (saved) {
            logger.info("saved");
        } else {
            logger.info("Not saved");
        }
        return "redirect:/appointments/";
    }


    @GetMapping("/details/{id}")
    public String getAppointmentDetailsPage(Model model, @PathVariable int id) {
        Appointment appointment = service.getAppointment(id);
        model.addAttribute("appointment", appointment);
        return "appointments/appointment_details_page";
    }


    @GetMapping("/delete/{id}")
    public String removeAppointment(@PathVariable int id) {
        if (service.removeAppointment(id)) {
            logger.debug("Appointment Deleted!");
        } else {
            logger.debug("Appointment still saved!");

        }
        return "redirect:/appointments/";
    }


    @ExceptionHandler(InvalidAppointmentException.class)
    public String handleInvalidAppointmentException(InvalidAppointmentException e, RedirectAttributes redirectAttributes) {
        logger.debug("InvalidAppointment Exception: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("exceptionMsg", e.getMessage());
        return "redirect:/appointments/add";
    }

    @ExceptionHandler(NationalNumberNotFoundException.class)
    public String handleNationalNumberNotFoundException(NationalNumberNotFoundException e, RedirectAttributes redirectAttributes) {
        logger.debug("NationalNumberNotFoundException: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("exceptionMsg", e.getMessage());
        return "redirect:/appointments/add";
    }


}
