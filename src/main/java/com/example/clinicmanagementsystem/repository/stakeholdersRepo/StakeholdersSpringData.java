package com.example.clinicmanagementsystem.repository.stakeholdersRepo;

import com.example.clinicmanagementsystem.domain.Doctor;
import com.example.clinicmanagementsystem.domain.Patient;
import com.example.clinicmanagementsystem.domain.Stakeholder;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StakeholdersJPA2 extends JpaRepository<Stakeholder, Integer> {


    @Override
    <S extends Stakeholder> @NotNull List<S> findAll(@NotNull Example<S> example);

    @Override
    @NotNull
    Optional<Stakeholder> findById(@NotNull Integer integer);


    @Query("SELECT p FROM Patient p WHERE p.nationalNumber = :nn")
    public Patient findPatientByNationalNumber(@Param("nn") String nationalNumber);


    @Query("SELECT DISTINCT p " +
            "FROM Appointment a " +
            "JOIN a.patient p " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDateTime > current_timestamp ")
    List<Patient> findDoctorPatients(int doctorId);

    @Query("SELECT DISTINCT d " +
            "FROM Appointment a " +
            "JOIN a.doctor d " +
            "WHERE a.patient.id = :patientId " +
            "AND a.appointmentDateTime > current_timestamp ")
    List<Doctor> findPatientDoctors(int patientId);

    @Override
    <S extends Stakeholder> @NotNull S save(@NotNull S entity);

    @Override
    void deleteById(@NotNull Integer integer);
}
