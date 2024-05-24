package com.example.clinicmanagementsystem.controllers.mvc.csv;

import com.example.clinicmanagementsystem.services.csvServices.ICsvService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/medications-csv")
public class MedicationsCsvController {

    private final ICsvService csvService;

    public MedicationsCsvController(ICsvService csvService) {
        this.csvService = csvService;
    }

    @GetMapping
    public ModelAndView uploadMedicationsCsv() {
        return new ModelAndView("medications-csv");
    }

    @PostMapping
    public ModelAndView uploadMedicationsCsv(@RequestParam("medications-csv") MultipartFile file) {
        ModelAndView mav = new ModelAndView("medications-csv");
        try {
            csvService.processMedicationsCsv(file.getInputStream());
            mav.addObject("message", "File uploaded and processed successfully!");
        } catch (IOException e) {
            mav.addObject("message", "Failed to process the file.");
            mav.addObject("error", e.getMessage());
        }
        return mav;
    }
}
