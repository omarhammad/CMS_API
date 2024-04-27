package com.example.clinicmanagementsystem.controllers.dtos.auth;

import java.util.List;

public class CurrentUserResponseDTO {

    private long userId;
    private String username;

    private List<String> userRoles;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }
}
