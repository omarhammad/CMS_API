package com.example.clinicmanagementsystem.controllers.api.auth;

import com.example.clinicmanagementsystem.controllers.dtos.auth.CurrentUserResponseDTO;
import com.example.clinicmanagementsystem.domain.CustomUserDetails;
import com.example.clinicmanagementsystem.controllers.dtos.auth.SignUpRequestDTO;
import com.example.clinicmanagementsystem.controllers.dtos.patients.PatientResponseDTO;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {


    private final IStakeholderService service;
    private final ModelMapper modelMapper;

    public AuthRestController(IStakeholderService services, ModelMapper modelMapper) {
        this.service = services;
        this.modelMapper = modelMapper;
    }


    @PostMapping("/signup")
    public ResponseEntity<PatientResponseDTO> patientSignup(@RequestBody @Valid SignUpRequestDTO requestDTO, HttpServletRequest request) {

        System.out.println("SIGNUP INFO : " + requestDTO);
        PatientResponseDTO responseDTO = null;
        try {
            responseDTO = service.addNewPatient(requestDTO.getFirstName(), requestDTO.getLastName(), requestDTO.getGender(), requestDTO.getNationalNumber(), requestDTO.getUsername(), requestDTO.getPassword());
            request.login(requestDTO.getUsername(), requestDTO.getPassword());
        } catch (ServletException e) {
            System.out.println(e.getMessage());
        }
        if (responseDTO == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }

    @GetMapping("/user/current")
    public ResponseEntity<CurrentUserResponseDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CurrentUserResponseDTO responseDTO = modelMapper.map(userDetails, CurrentUserResponseDTO.class);
        List<String> userRoles = userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).toList();
        responseDTO.setUserRoles(userRoles);
        return ResponseEntity.ok(responseDTO);

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
