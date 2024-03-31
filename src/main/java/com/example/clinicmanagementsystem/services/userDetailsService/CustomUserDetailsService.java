package com.example.clinicmanagementsystem.services.userDetailsService;

import com.example.clinicmanagementsystem.domain.CustomUserDetails;
import com.example.clinicmanagementsystem.domain.Stakeholder;
import com.example.clinicmanagementsystem.domain.User;
import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import com.example.clinicmanagementsystem.services.userService.IUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IStakeholderService service;

    public CustomUserDetailsService(IStakeholderService service) {
        this.service = service;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Stakeholder user = service.getStakeholderByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("USER NOT FOUND!");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        System.out.printf("USER ROLE IS : %s%n", user.getRole().getLabel());
        authorities.add(new SimpleGrantedAuthority(user.getRole().getLabel()));
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }

}
