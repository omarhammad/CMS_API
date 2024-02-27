package com.example.clinicmanagementsystem.repository.medicationsRepo;

import com.example.clinicmanagementsystem.domain.Medication;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MedicationsSpringData extends JpaRepository<Medication, Integer> {

    @Override
    <S extends Medication> @NotNull S save(@NotNull S entity);

    @Override
    void deleteById(@NotNull Integer integer);

    @Override
    <S extends Medication> @NotNull List<S> findAll(@NotNull Example<S> example);

    @Override
    @NotNull
    Optional<Medication> findById(@NotNull Integer integer);

    @Query("SELECT m FROM Medication m " +
            "JOIN m.prescriptions p " +
            "WHERE p.prescriptionId = :prescriptionId")
    List<Medication> getPrescriptionMedication(int prescriptionId);


}
