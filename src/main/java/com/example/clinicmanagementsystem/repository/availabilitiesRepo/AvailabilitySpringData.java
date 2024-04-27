package com.example.clinicmanagementsystem.repository.availabilitiesRepo;

import com.example.clinicmanagementsystem.domain.Availability;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AvailabilitySpringData extends JpaRepository<Availability, Integer> {

    @Override
    <S extends Availability> @NotNull S save(@NotNull S entity);


    @Override
    void deleteById(@NotNull Integer id);


    List<Availability> findAvailabilitiesByDoctor_IdAndUsedIsFalseAndSlotIsAfter(long doctor_id,LocalDateTime current);

}
