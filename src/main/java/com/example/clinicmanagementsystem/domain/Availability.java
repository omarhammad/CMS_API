package com.example.clinicmanagementsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "slot"}))
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime slot;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private boolean used;


    public Availability() {
    }

    public Availability(Long id, LocalDateTime slot, Doctor doctor, boolean used) {
        this.id = id;
        this.slot = slot;
        this.doctor = doctor;
        this.used = used;
    }


    public Availability(LocalDateTime slot, Doctor doctor, boolean used) {
        this.slot = slot;
        this.doctor = doctor;
        this.used = used;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getSlot() {
        return slot;
    }

    public void setSlot(LocalDateTime slot) {
        this.slot = slot;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "Availability{" +
                "id=" + id +
                ", slot=" + slot +
                ", doctor=" + doctor +
                ", used=" + used +
                '}';
    }
}
