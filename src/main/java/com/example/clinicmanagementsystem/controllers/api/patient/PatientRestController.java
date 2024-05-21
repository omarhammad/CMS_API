package com.example.clinicmanagementsystem.controllers.api.patient;

import com.example.clinicmanagementsystem.exceptions.NationalNumberExistException;
import com.example.clinicmanagementsystem.controllers.api.doctor.DoctorRestController;
import com.example.clinicmanagementsystem.controllers.dtos.patients.UpdatePatientRequestDTO;
import com.example.clinicmanagementsystem.controllers.dtos.patients.CreatePatientRequestDTO;
import com.example.clinicmanagementsystem.controllers.dtos.patients.PatientResponseDTO;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {


    private final IStakeholderService service;
    private final Logger logger;

    @Autowired
    public PatientRestController(IStakeholderService service) {
        this.service = service;
        logger = LoggerFactory.getLogger(DoctorRestController.class);
    }


    @GetMapping({""})
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {

        List<PatientResponseDTO> responseDTOS = service.getAllPatients();
        if (!responseDTOS.isEmpty()) {
            return ResponseEntity.ok(responseDTOS);
        }
        return new ResponseEntity<>(responseDTOS, HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatient(@PathVariable int id) {
        PatientResponseDTO responseDTO = service.getAPatient(id);
        if (responseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("")
    public ResponseEntity<PatientResponseDTO> addPatient(@RequestBody @Valid CreatePatientRequestDTO requestDTO) {

        String contactInfo = requestDTO.getPhoneNumber() + "," + requestDTO.getEmail();
        PatientResponseDTO responseDTO = service.addNewPatient(requestDTO.getFirstName(), requestDTO.getLastName(),
                requestDTO.getGender(), requestDTO.getNationalNumber(), requestDTO.getUsername(), requestDTO.getPassword(),contactInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePatient(@RequestBody @Valid UpdatePatientRequestDTO requestDTO, @PathVariable("id") int patientId) {
        logger.debug("PUT Req");
        if (patientId != requestDTO.getPatientId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (service.getAPatient(requestDTO.getPatientId()) == null) {
            return ResponseEntity.notFound().build();
        }

        String contactInfo = requestDTO.getPhoneNumber() + "," + requestDTO.getEmail();
        service.updatePatient(requestDTO.getPatientId(), requestDTO.getFirstName(),
                requestDTO.getLastName(), requestDTO.getGender(), requestDTO.getNationalNumber(),contactInfo);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable int id) {
        PatientResponseDTO patient = service.getAPatient(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isRemoved = service.removePatient(id);
        if (!isRemoved) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.noContent().build();


    }

    @ExceptionHandler(NationalNumberExistException.class)
    public ResponseEntity<HashMap<String, String>> handleNationalNumberExistException(NationalNumberExistException exception) {
        logger.debug("Entered to NationalNumberExistException: {}", exception.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingValue, newValue) -> existingValue + "; " + newValue
                ));

        return ResponseEntity.badRequest().body(errors);
    }
}
