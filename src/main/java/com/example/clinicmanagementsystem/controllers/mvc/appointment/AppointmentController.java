package com.example.clinicmanagementsystem.controllers.mvc.appointment;

import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.services.appointmentServices.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
