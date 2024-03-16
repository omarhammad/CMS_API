package com.example.clinicmanagementsystem.repository.appointmentsRepo;

import com.example.clinicmanagementsystem.Exceptions.InvalidAppointmentException;
import com.example.clinicmanagementsystem.domain.Appointment;
import com.example.clinicmanagementsystem.domain.Doctor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentsSpringData extends JpaRepository<Appointment, Long> {


    @Override
    @NotNull
    Optional<Appointment> findById(@NotNull Long id);

    @Override
    <S extends Appointment> @NotNull S save(@NotNull S entity) throws InvalidAppointmentException;

    @Query("SELECT d FROM Doctor d ")
    List<Doctor> findAllDoctorNames();


    @Override
    void deleteById(@NotNull Long id);

    @Query("SELECT a " +
            "FROM Appointment a " +
            "WHERE a.patient.id = :patientId " +
            "AND a.appointmentDateTime < current_timestamp ")
    List<Appointment> getPatientOldAppointments(int patientId);

    @Query("SELECT a " +
            "FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDateTime > current_timestamp ")
    List<Appointment> getDoctorUpComingAppointments(int doctorId);

}
