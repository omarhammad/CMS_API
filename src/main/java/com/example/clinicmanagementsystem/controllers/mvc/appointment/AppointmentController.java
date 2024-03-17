package com.example.clinicmanagementsystem.controllers.mvc.appointment;

import com.example.clinicmanagementsystem.Exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.domain.SessionHistory;
import com.example.clinicmanagementsystem.services.appointmentServices.IAppointmentService;
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
@RequestMapping("/appointments")
public class AppointmentController {

    IAppointmentService service;

    @Autowired
    public AppointmentController(IAppointmentService service) {
        this.service = service;
    }

    @GetMapping(value = {"/", ""})
    public String showAppointmentsPage() {
        return "appointments/appointments_page";
    }


    @GetMapping("/add")
    public String getAddAppointmentPage(Model model) {
        model.addAttribute("doctors", service.getDoctorsNames());
        model.addAttribute("appointmentTypes", AppointmentType.values());

        return "appointments/add-appointment";
    }


    @GetMapping("/details/{id}")
    public String loadAppointmentDetailsPage(@PathVariable long id) {
        return "appointments/appointment_details_page";
    }


    @GetMapping(value = {"/update/{id}"})
    public String showUpdateAppointmentPage(Model model, @PathVariable String id) {
        model.addAttribute("doctors", service.getDoctorsNames());
        model.addAttribute("appointmentTypes", AppointmentType.values());

        return "appointments/update_appointment_page";
    }

}
