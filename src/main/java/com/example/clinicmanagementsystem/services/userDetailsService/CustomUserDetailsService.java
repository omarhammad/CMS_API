package com.example.clinicmanagementsystem.services.userDetailsService;

import com.example.clinicmanagementsystem.domain.CustomUserDetails;
import com.example.clinicmanagementsystem.domain.User;
import com.example.clinicmanagementsystem.services.userService.IUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    IUserService service;

    public CustomUserDetailsService(IUserService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("USER NOT FOUND!");
        }
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), Collections.emptyList());
    }

}
