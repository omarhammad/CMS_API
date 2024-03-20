package com.example.clinicmanagementsystem.controllers.api.prescription;

import com.example.clinicmanagementsystem.dtos.prescriptions.PrescriptionResponseDTO;
import com.example.clinicmanagementsystem.dtos.prescriptions.PrescriptionsRequestDTO;
import com.example.clinicmanagementsystem.services.treatementServices.ITreatmentService;
import com.example.clinicmanagementsystem.services.treatementServices.TreatmentSvc;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
