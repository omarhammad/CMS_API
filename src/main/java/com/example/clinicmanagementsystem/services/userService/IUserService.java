package com.example.clinicmanagementsystem.services.userService;


import com.example.clinicmanagementsystem.domain.User;

public interface IUserService {


    void createNewUser(String username, String password);

    User findUserByUsername(String username);

    boolean deleteUser(String username);

    User findUserById(Long id);

}
