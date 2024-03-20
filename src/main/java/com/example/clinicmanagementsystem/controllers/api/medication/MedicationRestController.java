package com.example.clinicmanagementsystem.controllers.api.medication;

import com.example.clinicmanagementsystem.Exceptions.ContactInfoExistException;
import com.example.clinicmanagementsystem.Exceptions.MedicationDeletionException;
import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.domain.util.MedicationForm;
import com.example.clinicmanagementsystem.domain.util.Unit;
import com.example.clinicmanagementsystem.dtos.medications.CreateMedicationRequestDTO;
import com.example.clinicmanagementsystem.dtos.medications.MedicationResponseDTO;
import com.example.clinicmanagementsystem.dtos.medications.UpdateMedicationRequestDTO;
import com.example.clinicmanagementsystem.services.treatementServices.ITreatmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medications")
public class MedicationRestController {


    private final ITreatmentService service;
    private final Logger logger;

    public MedicationRestController(ITreatmentService service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(MedicationRestController.class);
    }


    @GetMapping({"/", ""})
    public ResponseEntity<List<MedicationResponseDTO>> getAllMedications() {
        logger.debug("Loading All medications Data");
        List<MedicationResponseDTO> responseDTOS = service.getAllMedications();
        if (responseDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseDTOS);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> getMedication(@PathVariable int id) {
        MedicationResponseDTO responseDTO = service.getMedication(id);
        if (responseDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping({"/", ""})
    public ResponseEntity<Void> addNewMedication(@RequestBody @Valid CreateMedicationRequestDTO requestDTO) throws SQLException {
        Medication medication = service.addNewMedication(requestDTO.getName(), MedicationForm.valueOf(requestDTO.getMedicationForm()),
                requestDTO.getQuantity(), Unit.valueOf(requestDTO.getUnit()), requestDTO.getFrequencies(),
                requestDTO.getDaysDuration(), requestDTO.getNotes());
        if (medication == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMedication(@RequestBody @Valid UpdateMedicationRequestDTO requestDTO, @PathVariable int id) {

        if (requestDTO.getMedicationId() != id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        boolean isFound = service.getMedication(id) != null;
        if (!isFound) {
            return ResponseEntity.notFound().build();
        }

        Medication medication = service.updateMedication(requestDTO.getMedicationId(), requestDTO.getName(), MedicationForm.valueOf(requestDTO.getMedicationForm()),
                requestDTO.getQuantity(), Unit.valueOf(requestDTO.getUnit()), requestDTO.getFrequencies(),
                requestDTO.getDaysDuration(), requestDTO.getNotes());


        if (medication == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable int id) {

        boolean isFound = service.getMedication(id) != null;

        if (!isFound) {
            return ResponseEntity.notFound().build();
        }

        service.removeMedication(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MedicationDeletionException.class)
    public ResponseEntity<HashMap<String, String>> medicationDeletionException(MedicationDeletionException exception) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", exception.getMessage());
        System.out.println(errors.get("exceptionMsg"));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingValue, newValue) -> existingValue + ";" + newValue
                ));

        return ResponseEntity.badRequest().body(errors);
    }
}
