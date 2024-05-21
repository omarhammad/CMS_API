package com.example.clinicmanagementsystem.services.stakeholdersServices;

import com.example.clinicmanagementsystem.exceptions.ContactInfoExistException;
import com.example.clinicmanagementsystem.exceptions.NationalNumberExistException;
import com.example.clinicmanagementsystem.controllers.dtos.doctors.AvailabilityResponseDTO;
import com.example.clinicmanagementsystem.domain.*;
import com.example.clinicmanagementsystem.controllers.dtos.appointments.AppointmentResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.doctors.DoctorDetailsResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.doctors.DoctorResponseDTO;
import com.example.clinicmanagementsystem.controllers.dtos.patients.PatientResponseDTO;
import com.example.clinicmanagementsystem.exceptions.SlotUsedException;
import com.example.clinicmanagementsystem.repository.appointmentsRepo.AppointmentsSpringData;
import com.example.clinicmanagementsystem.repository.availabilitiesRepo.AvailabilitySpringData;
import com.example.clinicmanagementsystem.repository.stakeholdersRepo.StakeholdersSpringData;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class StakeholderSvc implements IStakeholderService {


    private StakeholdersSpringData stakeholdersRepo;
    private AppointmentsSpringData appointmentsRepo;

    private AvailabilitySpringData availabilityRepo;
    private ModelMapper modelMapper;

    private final BCryptPasswordEncoder encoder;


    public StakeholderSvc(StakeholdersSpringData stakeholdersRepo, AppointmentsSpringData appointmentsRepo, AvailabilitySpringData availabilityRepo, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.stakeholdersRepo = stakeholdersRepo;
        this.appointmentsRepo = appointmentsRepo;
        this.availabilityRepo = availabilityRepo;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = stakeholdersRepo.findAll().stream()
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .toList();

        List<DoctorResponseDTO> doctorResponseDTOS = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorResponseDTOS.add(modelMapper.map(doctor, DoctorResponseDTO.class));
        }
        return doctorResponseDTOS;

    }

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = stakeholdersRepo.findAll().stream()
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .toList();

        List<PatientResponseDTO> patientResponseDTOS = new ArrayList<>();

        for (Patient patient : patients) {
            patientResponseDTOS.add(modelMapper.map(patient, PatientResponseDTO.class));
        }
        return patientResponseDTOS;
    }

    @Override
    public DoctorResponseDTO getADoctor(long doctorId) {
        Doctor doctor = ((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        if (doctor == null) {
            return null;
        }
        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }

    @Override
    public DoctorDetailsResponseDTO getFullDoctorData(long doctorId) {
        Doctor doctor = ((Doctor) stakeholdersRepo.findById(doctorId).orElse(null));
        DoctorDetailsResponseDTO doctorDetailsResponseDTO = modelMapper.map(doctor, DoctorDetailsResponseDTO.class);
        List<AppointmentResponseDTO> appointmentResponseDTOS = new ArrayList<>();
        for (Appointment appointment : appointmentsRepo.getDoctorUpComingAppointments(doctorId)) {
            appointmentResponseDTOS.add(modelMapper.map(appointment, AppointmentResponseDTO.class));
        }

        doctorDetailsResponseDTO.setAppointments(appointmentResponseDTOS);
        return doctorDetailsResponseDTO;
    }

    @Override
    public PatientResponseDTO getAPatient(long patientId) {
        Patient patient = ((Patient) stakeholdersRepo.findById(patientId).orElse(null));

        if (patient == null) {
            return null;
        }

        List<Doctor> doctors = getPatientDoctors(patientId);

        List<DoctorResponseDTO> doctorResponseDTOS = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorResponseDTOS.add(modelMapper.map(doctor, DoctorResponseDTO.class));
        }

        PatientResponseDTO responseDTO = modelMapper.map(patient, PatientResponseDTO.class);
        responseDTO.setHisDoctors(doctorResponseDTOS);

        List<Appointment> oldAppointments = appointmentsRepo.getPatientOldAppointments(patientId);

        List<AppointmentResponseDTO> oldAppointmentResponseDTOS = new ArrayList<>();
        for (Appointment appointment : oldAppointments) {
            oldAppointmentResponseDTOS.add(modelMapper.map(appointment, AppointmentResponseDTO.class));
        }
        responseDTO.setOldAppointments(oldAppointmentResponseDTOS);
        return responseDTO;
    }

    @Override
    public DoctorResponseDTO addNewDoctor(String firstName, String lastName, String specialization, String contactInfo, String username, String password) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialization(specialization);
        doctor.setContactInfo(contactInfo);
        doctor.setUsername(username);
        doctor.setPassword(encoder.encode(password));
        doctor.setRole(UserRole.DOCTOR);
        Doctor addedDoctor;

        try {
            addedDoctor = stakeholdersRepo.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new ContactInfoExistException(contactInfo);
        }

        return modelMapper.map(addedDoctor, DoctorResponseDTO.class);
    }

    @Override
    public void updateADoctor(long id, String firstName, String lastName, String specialization, String contactInfo) {
        Doctor doctor = ((Doctor) stakeholdersRepo.findById(id).orElse(null));
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialization(specialization);
        doctor.setContactInfo(contactInfo);

        try {
            stakeholdersRepo.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new ContactInfoExistException(contactInfo);
        }
    }

    @Override
    public PatientResponseDTO addNewPatient(String firstName, String lastName, String gender, String nationalNumber, String username, String password, String contactInfo) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setGender(gender);
        patient.setNationalNumber(nationalNumber);
        patient.setContactInfo(contactInfo);

        String[] nDateOfBirth = nationalNumber.split("-")[0].split("\\.");
        int currentYearDigits = LocalDate.now().getYear() % 100;
        int century = (Integer.parseInt(nDateOfBirth[0]) <= currentYearDigits) ? 2000 : 1900;
        int year = century + Integer.parseInt(nDateOfBirth[0]);
        LocalDate patientDob = LocalDate.of(year, Integer.parseInt(nDateOfBirth[1]), Integer.parseInt(nDateOfBirth[2]));
        LocalDate current = LocalDate.now();
        int age = Period.between(patientDob, current).getYears();
        patient.setAge(age);
        patient.setUsername(username);
        patient.setPassword(encoder.encode(password));
        patient.setRole(UserRole.PATIENT);

        PatientResponseDTO patientResponseDTO = null;

        try {
            Patient savedPatient = stakeholdersRepo.save(patient);
            patientResponseDTO = modelMapper.map(savedPatient, PatientResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new NationalNumberExistException(nationalNumber);
        }
        return patientResponseDTO;
    }

    @Override
    public int removeDoctor(long doctorId) {
        if (stakeholdersRepo.findById(doctorId).isEmpty()) {
            return 404;
        }
        stakeholdersRepo.deleteById(doctorId);
        return stakeholdersRepo.findById(doctorId).isEmpty() ? 204 : 501;
    }

    @Override
    public boolean removePatient(long patientId) {
        stakeholdersRepo.deleteById(patientId);
        return stakeholdersRepo.findById(patientId).isEmpty();
    }


    @Override
    public List<Doctor> getPatientDoctors(long patientId) {
        return stakeholdersRepo.findPatientDoctors(patientId);
    }


    @Override
    public void updatePatient(long patientId, String firstName, String lastName, String gender, String nationalNumber, String contactInfo) {
        Patient patient = ((Patient) stakeholdersRepo.findById(patientId).orElse(null));
        patient.setId(patientId);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setGender(gender);
        patient.setNationalNumber(nationalNumber);
        patient.setContactInfo(contactInfo);

        String[] nDateOfBirth = nationalNumber.split("-")[0].split("\\.");
        int currentYearDigits = LocalDate.now().getYear() % 100;
        int century = (Integer.parseInt(nDateOfBirth[0]) <= currentYearDigits) ? 2000 : 1900;
        int year = century + Integer.parseInt(nDateOfBirth[0]);
        LocalDate patientDob = LocalDate.of(year, Integer.parseInt(nDateOfBirth[1]), Integer.parseInt(nDateOfBirth[2]));
        LocalDate current = LocalDate.now();
        int age = Period.between(patientDob, current).getYears();
        patient.setAge(age);


        try {

            stakeholdersRepo.save(patient);
        } catch (DataIntegrityViolationException e) {
            throw new NationalNumberExistException(nationalNumber);
        }
    }

    @Override
    public Stakeholder getStakeholderByUsername(String username) {
        return stakeholdersRepo.findStakeholderByUsername(username).orElse(null);
    }

    @Override
    public List<AvailabilityResponseDTO> getDoctorAvailablilities(long id) {
        List<Availability> availabilities = availabilityRepo
                .findAvailabilitiesByDoctor_IdAndUsedIsFalseAndSlotIsAfter(id, LocalDateTime.now().plusMinutes(30));

        List<AvailabilityResponseDTO> availabilityResponseDTOS = new ArrayList<>();
        for (Availability availability : availabilities) {
            DoctorResponseDTO doctor = modelMapper.map(availability.getDoctor(), DoctorResponseDTO.class);
            AvailabilityResponseDTO availabilityResponseDTO = modelMapper.map(availability, AvailabilityResponseDTO.class);
            availabilityResponseDTO.setDoctor(doctor);
            availabilityResponseDTOS.add(availabilityResponseDTO);
        }
        return availabilityResponseDTOS;
    }

    @Override
    public AvailabilityResponseDTO addDoctorAvailability(long id, LocalDateTime slot) {
        Availability availability = new Availability();
        Doctor doctor = ((Doctor) stakeholdersRepo.findById(id).orElse(null));
        availability.setDoctor(doctor);
        availability.setSlot(slot);
        availability.setUsed(false);
        AvailabilityResponseDTO responseDTO;
        try {
            Availability savedAvailability = availabilityRepo.save(availability);
            responseDTO = modelMapper.map(savedAvailability, AvailabilityResponseDTO.class);
            responseDTO.setDoctor(modelMapper.map(savedAvailability.getDoctor(), DoctorResponseDTO.class));

        } catch (DataIntegrityViolationException e) {
            System.out.println("ExceptionReached!");
            throw new SlotUsedException("Used! choose another slot");
        }
        return responseDTO;
    }

    @Override
    public AvailabilityResponseDTO getDoctorAvailability(int availabilityId) {
        Availability availability = availabilityRepo.findById(availabilityId).orElse(null);
        return modelMapper.map(availability, AvailabilityResponseDTO.class);
    }

    @Override
    public void removeAvailability(int availabilityId) {
        availabilityRepo.deleteById(availabilityId);
    }


    @Override
    public AvailabilityResponseDTO getAvailability(int availabilityId) {
        Availability availability = availabilityRepo.findById(availabilityId).orElseThrow(() -> new EntityNotFoundException("Availability Not Found!"));
        return modelMapper.map(availability, AvailabilityResponseDTO.class);
    }


}
