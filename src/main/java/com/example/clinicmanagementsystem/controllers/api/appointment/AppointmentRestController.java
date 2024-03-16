package com.example.clinicmanagementsystem.controllers.api.appointment;

import com.example.clinicmanagementsystem.Exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.Exceptions.NationalNumberNotFoundException;
import com.example.clinicmanagementsystem.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.dtos.appointments.CreateAppointmentRequestDTO;
import com.example.clinicmanagementsystem.dtos.appointments.UpdateAppointmentRequestDTO;
import com.example.clinicmanagementsystem.services.appointmentServices.IAppointmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {


    private final Logger logger;
    private final IAppointmentService service;

    public AppointmentRestController(IAppointmentService service) {
        this.logger = LoggerFactory.getLogger(AppointmentRestController.class);
        this.service = service;
    }


    @GetMapping({"/", ""})
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments() {

        logger.debug("LOADING ALL APPOINTMENTS");
        List<AppointmentResponseDTO> appointments = service.getAllAppointment();
        // If the list is empty I return a response with noContent status
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Return a response with all appointments
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointment(@PathVariable long id) {

        logger.debug("Loading Appointment with id : {}", id);
        AppointmentResponseDTO appointmentResponseDTO = service.getAppointment(id);
        if (appointmentResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appointmentResponseDTO);

    }

    @PostMapping("")
    public ResponseEntity<AppointmentResponseDTO> addNewAppointment(@RequestBody @Valid CreateAppointmentRequestDTO requestDTO) {

        // Adding a new appointment and return a response ok with the appointment that contains the ID.
        AppointmentResponseDTO appointmentResponseDTO = service.addNewAppointment(requestDTO.getDoctor(), requestDTO.getPatientNN(),
                requestDTO.getAppointmentDateTime(), requestDTO.getPurpose(), requestDTO.getAppointmentType());

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAppointment(@PathVariable long id, @RequestBody @Valid UpdateAppointmentRequestDTO requestDTO) {
        if (requestDTO.getId() != id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (service.getAppointment(id) == null) {
            return ResponseEntity.notFound().build();
        }

        service.updateAppointment(requestDTO.getId(), requestDTO.getDoctor(), requestDTO.getPatientNN(),
                requestDTO.getAppointmentDateTime(), requestDTO.getPurpose(), requestDTO.getAppointmentType());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable long id) {

        AppointmentResponseDTO appointment = service.getAppointment(id);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        service.removeAppointment(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<HashMap<String, String>> handleInvalidAppointmentException(InvalidAppointmentException e, RedirectAttributes redirectAttributes) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NationalNumberNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> handleNationalNumberNotFoundException(NationalNumberNotFoundException e, RedirectAttributes redirectAttributes) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
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
