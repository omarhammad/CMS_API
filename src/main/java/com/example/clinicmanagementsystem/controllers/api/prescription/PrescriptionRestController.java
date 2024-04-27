package com.example.clinicmanagementsystem.controllers.api.prescription;

import com.example.clinicmanagementsystem.controllers.dtos.prescriptions.PrescriptionResponseDTO;
import com.example.clinicmanagementsystem.services.treatementServices.ITreatmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionRestController {


    private final ITreatmentService service;

    public PrescriptionRestController(ITreatmentService service) {
        this.service = service;
    }


    @GetMapping({"/", ""})
    public ResponseEntity<List<PrescriptionResponseDTO>> getAllPrescriptions() {
        List<PrescriptionResponseDTO> responseDTOS = service.getAllPrescriptions();
        if (responseDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseDTOS);
    }

}
