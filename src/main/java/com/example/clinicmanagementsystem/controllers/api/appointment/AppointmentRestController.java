package com.example.clinicmanagementsystem.controllers.api.appointment;

import com.example.clinicmanagementsystem.exceptions.*;
import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.CreateAppointmentRequestDTO;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.UpdateAppointmentRequestDTO;
import com.example.clinicmanagementsystem.controllers.dtos.patients.PatientResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.prescriptions.PrescriptionResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.prescriptions.PrescriptionsRequestDTO;
import com.example.clinicmanagementsystem.services.appointmentServices.IAppointmentService;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import com.example.clinicmanagementsystem.services.treatementServices.ITreatmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {


    private final Logger logger;
    private final IAppointmentService appointmentService;
    private final IStakeholderService stakeholderService;
    private final ITreatmentService treatmentService;

    public AppointmentRestController(IAppointmentService service, IStakeholderService stakeholderService, ITreatmentService treatmentService) {
        this.stakeholderService = stakeholderService;
        this.treatmentService = treatmentService;
        this.logger = LoggerFactory.getLogger(AppointmentRestController.class);
        this.appointmentService = service;
    }


    @GetMapping({"/", ""})
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments() {

        logger.debug("LOADING ALL APPOINTMENTS");
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointment();
        // If the list is empty I return a response with noContent status
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Return a response with all appointments
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientAppointment(@PathVariable int id) {


        PatientResponseDTO patient = stakeholderService.getAPatient(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        List<AppointmentResponseDTO> responseDTOS = appointmentService.getPatientAppointments(id);
        if (responseDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/doctor/{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointment(@PathVariable int id) {

        boolean doctorExists = stakeholderService.getADoctor(id) != null;
        if (!doctorExists) {
            return ResponseEntity.notFound().build();
        }
        List<AppointmentResponseDTO> responseDTOS = appointmentService.getDoctorAppointments(id);
        if (responseDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointment(@PathVariable long id) {

        logger.debug("Loading Appointment with id : {}", id);
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.getAppointment(id);
        if (appointmentResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        if (appointmentResponseDTO.getPrescription() != null) {
            logger.debug("{}", appointmentResponseDTO.getPrescription().getMedications());


        }
        return ResponseEntity.ok(appointmentResponseDTO);
    }

    @GetMapping("/{appId}/prescription/{id}")
    public ResponseEntity<PrescriptionResponseDTO> getAppointmentPrescription(@PathVariable int appId, @PathVariable long id) {
        PrescriptionResponseDTO responseDTO = treatmentService.getPrescription(id);
        AppointmentResponseDTO appointment = appointmentService.getAppointment(appId);

        if (responseDTO == null || appointment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{appId}/prescription")
    public ResponseEntity<Void> addAppointmentPrescription(@RequestBody @Valid PrescriptionsRequestDTO requestDTO, @PathVariable String appId) {
        logger.debug("{}", requestDTO);
        treatmentService.addNewPrescription(requestDTO.getMedicationsIds(), requestDTO.getExpireDate(), requestDTO.getAppointmentId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping({"", "/"})
    public ResponseEntity<AppointmentResponseDTO> addNewAppointment(@RequestBody @Valid CreateAppointmentRequestDTO requestDTO) {

        logger.debug("DTO : {}", requestDTO);

        // Adding a new appointment and return a response ok with the appointment that contains the ID.
            AppointmentResponseDTO appointmentResponseDTO = appointmentService.addNewAppointment(requestDTO.getDoctor(), requestDTO.getPatientNN(),
                    requestDTO.getAppointmentSlotId(), requestDTO.getPurpose(), AppointmentType.valueOf(requestDTO.getAppointmentType()));
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAppointment(@PathVariable long id, @RequestBody @Valid UpdateAppointmentRequestDTO requestDTO) {
        if (requestDTO.getAppointmentId() != id) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (appointmentService.getAppointment(id) == null) {
            return ResponseEntity.notFound().build();
        }

        appointmentService.updateAppointment(requestDTO.getAppointmentId(), requestDTO.getDoctor(), requestDTO.getPatientNN(),
                requestDTO.getAppointmentSlotId(), requestDTO.getPurpose(), AppointmentType.valueOf(requestDTO.getAppointmentType()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable long id) {

        AppointmentResponseDTO appointment = appointmentService.getAppointment(id);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        appointmentService.removeAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{appId}/prescription/{presId}")
    public ResponseEntity<Void> deleteAppointmentPrescription(@PathVariable long appId, @PathVariable long presId) {

        AppointmentResponseDTO appointment = appointmentService.getAppointment(appId);
        PrescriptionResponseDTO prescription = treatmentService.getPrescription(presId);

        if (appointment == null || prescription == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isRemoved = treatmentService.removePrescription(presId);
        if (!isRemoved) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<HashMap<String, String>> handleInvalidAppointmentException(InvalidAppointmentException e) {

        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(WrongSlotException.class)
    public ResponseEntity<HashMap<String, String>> handleWrongSlotException(WrongSlotException e) {

        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NationalNumberNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> handleNationalNumberNotFoundException(NationalNumberNotFoundException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> handleDoctorNotFoundExceptionException(DoctorNotFoundException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> handleAppointmentNotFoundException(AppointmentNotFoundException e) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("exceptionMsg", e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(SlotUsedException.class)
    public ResponseEntity<HashMap<String, String>> handleSlotUsedException(SlotUsedException e) {
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
                        (existingValue, newValue) -> existingValue + ";" + newValue
                ));

        return ResponseEntity.badRequest().body(errors);
    }
}
