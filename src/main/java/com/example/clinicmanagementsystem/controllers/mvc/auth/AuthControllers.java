package com.example.clinicmanagementsystem.controllers.mvc.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

