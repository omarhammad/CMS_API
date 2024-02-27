package com.example.clinicmanagementsystem.repository.prescriptionRepo;

import com.example.clinicmanagementsystem.domain.Prescription;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PrescriptionsSpringData extends JpaRepository<Prescription, Integer> {
    @Override
    <S extends Prescription> @NotNull List<S> findAll(@NotNull Example<S> example);

    @Override
    @NotNull
    Optional<Prescription> findById(@NotNull Integer integer);


    @Query("SELECT p " +
            "FROM Appointment a " +
            "JOIN a.prescription p " +
            "WHERE a.appointmentId = :appointmentId")
    public Prescription getAppointmentPrescription(int appointmentId);

    @Override
    <S extends Prescription> @NotNull S save(@NotNull S entity);


    @Override
    void deleteById(@NotNull Integer integer);

    @Modifying
    @Transactional
    @Query("DELETE FROM Prescription p WHERE p.appointment.appointmentId = :appointmentId")
    public boolean deletePrescriptionByAppointment(int appointmentId);


}
