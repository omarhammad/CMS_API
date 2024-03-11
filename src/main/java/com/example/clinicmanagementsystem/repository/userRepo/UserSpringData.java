package com.example.clinicmanagementsystem.repository.userRepo;

import com.example.clinicmanagementsystem.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSpringData extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
