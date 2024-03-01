package com.example.clinicmanagementsystem.controllers.doctor;

import com.example.clinicmanagementsystem.Exceptions.ContactInfoExistException;
import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.dtos.doctors.CreateDoctorRequestDTO;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorDetailsResponseDTO;
import com.example.clinicmanagementsystem.dtos.doctors.DoctorResponseDTO;
import com.example.clinicmanagementsystem.dtos.doctors.UpdateDoctorRequestDTO;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/api/doctors")
public class DoctorController {


    private final IStakeholderService service;
    private final Logger logger;

    @Autowired
    public DoctorController(IStakeholderService service) {
        this.service = service;
        logger = LoggerFactory.getLogger(DoctorController.class);
    }


    @GetMapping({""})
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {

        List<DoctorResponseDTO> doctorList = service.getAllDoctors();
        if (!doctorList.isEmpty()) {
            return ResponseEntity.ok(doctorList);
        }
        return new ResponseEntity<>(doctorList, HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDetailsResponseDTO> getDoctorById(@PathVariable int id) {
        DoctorDetailsResponseDTO doctor = service.getFullDoctorData(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }


    @PostMapping("")
    public ResponseEntity<DoctorResponseDTO> addNewDoctor(@RequestBody @Valid CreateDoctorRequestDTO createDoctorRequestDTO) {

        String contactInfo = createDoctorRequestDTO.getPhoneNumber() + "," + createDoctorRequestDTO.getEmail();
        DoctorResponseDTO doctorResponseDTO = service.addNewDoctor(createDoctorRequestDTO.getFirstName(), createDoctorRequestDTO.getLastName(), createDoctorRequestDTO.getSpecialization(), contactInfo);

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
