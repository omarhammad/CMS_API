package com.example.clinicmanagementsystem.controllers.mvc.auth;

import com.example.clinicmanagementsystem.services.stakeholdersServices.IStakeholderService;
import com.example.clinicmanagementsystem.services.userService.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthControllers {


    @GetMapping("/signin")
    public String showSignIn() {
        return "auth/sign_in_page";
    }

    @GetMapping("/signout")
    public String signOut(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "auth/sign_up_page";
    }


}

