package com.example.clinicmanagementsystem.presentation.doctorDetails;

import com.example.clinicmanagementsystem.Exceptions.ContactInfoExistException;
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
@RequestMapping("/doctors")
public class DoctorDetailsController {


    private final StakeholderService service;
    private final Logger logger;

    @Autowired
    public DoctorDetailsController(StakeholderService service) {
        this.service = service;
        logger = LoggerFactory.getLogger(DoctorDetailsController.class);
    }


    @GetMapping({"/", ""})
    public String getDoctorsPage(Model model, HttpSession session) {

        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }

        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Doctor Page"));
        logger.debug("Session History {}", sessionHistories);

        logger.debug("Loading Doctor Page....");
        List<Doctor> doctorList = service.getAllDoctors();
        model.addAttribute("doctors", doctorList);
        return "doctors/doctors_page";
    }

    @GetMapping("/add")
    public String getAddDoctorPage(Model model, HttpSession session) {
        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories = (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        sessionHistories.add(new SessionHistory(LocalDateTime.now(), "Add Doctors Page"));
        logger.debug("Session History {}", sessionHistories);


        logger.debug("Loading Doctors Page.....");
        model.addAttribute("doctorModel", new DoctorViewModel());
        return "doctors/add_new_doctor";
    }


    @PostMapping("/add")
    public String addDoctorPage(@Valid @ModelAttribute("doctorModel") DoctorViewModel doctorModel, BindingResult errors) {


        logger.debug("Adding new Doctor: {}", doctorModel);
        if (errors.hasErrors()) {
            return "doctors/add_new_doctor";
        }
        String contactInfo = doctorModel.getPhoneNumber() + "," + doctorModel.getEmail();
        service.addNewDoctor(doctorModel.getFirstName(), doctorModel.getLastName(), doctorModel.getSpecialization(), contactInfo);
        return "redirect:/doctors/";
    }

    @GetMapping("/details/{id}")
    public String getAppointmentDetailsPage(Model model, @PathVariable int id) {
        Doctor doctor = service.getADoctor(id);
        List<Patient> patients = service.getDoctorPatients(id);
        model.addAttribute("doctor", doctor);
        model.addAttribute("hisPatients", patients);

        return "doctors/doctor_details_page";
    }


    @GetMapping("/delete/{id}")
    public String removeDoctor(@PathVariable int id) {
        if (service.removeDoctor(id)) {
            logger.debug("Doctor Deleted!");
        } else {
            logger.debug("Doctor still saved!");

        }
        return "redirect:/doctors/";
    }


    @ExceptionHandler(ContactInfoExistException.class)
    public String handleContactInfoExistException(ContactInfoExistException exception, RedirectAttributes redirectAttributes) {
        logger.debug("ContactInfoExistException: {}", exception.getMessage());
        redirectAttributes.addFlashAttribute("exceptionMsg", exception.getMessage());
        return "redirect:/doctors/add";
    }
}
