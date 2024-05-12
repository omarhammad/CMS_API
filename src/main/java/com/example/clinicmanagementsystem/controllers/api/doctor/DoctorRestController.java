package com.example.clinicmanagementsystem.controllers.api.doctor;

import com.example.clinicmanagementsystem.exceptions.ContactInfoExistException;
import com.example.clinicmanagementsystem.controllers.dtos.doctors.*;
import com.example.clinicmanagementsystem.exceptions.SlotUsedException;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorRestController {


    private final IStakeholderService service;
    private final Logger logger;

    @Autowired
    public DoctorRestController(IStakeholderService service) {
        this.service = service;
        logger = LoggerFactory.getLogger(DoctorRestController.class);
    }


    @GetMapping({""})
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctors(@RequestParam(value = "searchTerm", required = false) final String searchTerm) {

        List<DoctorResponseDTO> doctorList;
        try {
            doctorList = service.getAllDoctors();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        if (searchTerm != null && !searchTerm.isEmpty())
            doctorList = doctorList.stream()
                    .filter(doctorResponseDTO -> {
                        String fullName = doctorResponseDTO.getFirstName() + " " + doctorResponseDTO.getLastName();
                        return fullName.toLowerCase().contains(searchTerm.toLowerCase());
                    }).toList();

        System.out.println("Doctor list size: " + doctorList.size());
        if (doctorList.isEmpty())
            return new ResponseEntity<>(doctorList, HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(doctorList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DoctorDetailsResponseDTO> getDoctorById(@PathVariable int id) {

        DoctorDetailsResponseDTO doctor = service.getFullDoctorData(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }


    @GetMapping("/{id}/availability")
    public ResponseEntity<List<AvailabilityResponseDTO>> getDoctorAvailabilities(@PathVariable long id) {

        DoctorResponseDTO doctorResponseDTO = service.getADoctor(id);
        if (doctorResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        List<AvailabilityResponseDTO> availabilityResponseDTOS = service.getDoctorAvailablilities(id);
        if (availabilityResponseDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("Reached!");
        System.out.println(availabilityResponseDTOS);
        return ResponseEntity.ok(availabilityResponseDTOS);
    }

    @PostMapping("/{id}/availability")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<AvailabilityResponseDTO> addDoctorAvailability(@RequestBody @Valid CreateAvailabilityRequestDTO requestDTO,
                                                                         @PathVariable long id) {
        System.out.println(requestDTO);
        AvailabilityResponseDTO responseDTO = service.addDoctorAvailability(id, requestDTO.getSlot());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    @DeleteMapping("/availability/{availabilityId}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<Void> deleteDoctorAvailabilityById(@PathVariable int availabilityId) {
        AvailabilityResponseDTO responseDTO = service.getDoctorAvailability(availabilityId);
        if (responseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        service.removeAvailability(availabilityId);
        return ResponseEntity.noContent().build();

    }


    @PostMapping("")
    public ResponseEntity<DoctorResponseDTO> addNewDoctor(@RequestBody @Valid CreateDoctorRequestDTO createDoctorRequestDTO) {

        String contactInfo = createDoctorRequestDTO.getPhoneNumber() + "," + createDoctorRequestDTO.getEmail();
        DoctorResponseDTO doctorResponseDTO = service.addNewDoctor(createDoctorRequestDTO.getFirstName(), createDoctorRequestDTO.getLastName()
                , createDoctorRequestDTO.getSpecialization(), contactInfo, createDoctorRequestDTO.getUsername(), createDoctorRequestDTO.getPassword());
        return ResponseEntity.ok(doctorResponseDTO);

    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDoctor(@RequestBody @Valid UpdateDoctorRequestDTO updateDoctorRequestDTO, @PathVariable("id") int id) {
        logger.debug("Reached! PUT");

        if (id != updateDoctorRequestDTO.getId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (service.getADoctor(updateDoctorRequestDTO.getId()) == null) {
            return ResponseEntity.notFound().build();
        }

        String contactInfo = updateDoctorRequestDTO.getPhoneNumber() + "," + updateDoctorRequestDTO.getEmail();

        service.updateADoctor(updateDoctorRequestDTO.getId(), updateDoctorRequestDTO.getFirstName(),
                updateDoctorRequestDTO.getLastName(), updateDoctorRequestDTO.getSpecialization(), contactInfo);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable int id) {
        int status = service.removeDoctor(id);
        if (status == 204) {
            logger.debug("Doctor Deleted!");
            return ResponseEntity.noContent().build();
        } else if (status == 404) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }


    @ExceptionHandler(ContactInfoExistException.class)
    public ResponseEntity<HashMap<String, String>> handleContactInfoExistException(ContactInfoExistException exception) {
        logger.debug("Entered to ContactInfoExistException: {}", exception.getMessage());
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(SlotUsedException.class)
    public ResponseEntity<HashMap<String, String>> handleSlotUsedException(SlotUsedException exception) {
        logger.debug("Entered to SlotUsedException: {}", exception.getMessage());
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
