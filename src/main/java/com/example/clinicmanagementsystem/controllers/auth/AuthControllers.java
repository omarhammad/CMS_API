package com.example.clinicmanagementsystem.controllers.auth;

import com.example.clinicmanagementsystem.services.userService.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthControllers {
    private Logger logger;
    private IUserService service;


    public AuthControllers(IUserService service) {
        this.logger = LoggerFactory.getLogger(AuthControllers.class);
        this.service = service;
    }

    @GetMapping("/signin")
    public String showSignIn(Model model) {
        model.addAttribute("loginModel", new LoginViewModel());
        return "auth/sign_in_page";
    }

    @GetMapping("/signout")
    public String signOut(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String showSignUp(Model model) {
        model.addAttribute("registerModel", new RegisterViewModel());
        return "auth/sign_up_page";
    }

    @PostMapping("/signup")
    public String addNewUser(@ModelAttribute("registerModel") @Valid RegisterViewModel viewModel, BindingResult errors, HttpServletRequest request) {

        logger.debug("Entered!! {} {} {}", viewModel.getUsername(), viewModel.getPassword(), viewModel.getConfirmPassword());

        if (service.findUserByUsername(viewModel.getUsername()) != null) {
            errors.rejectValue("username", "username.exists", "Username already exists!");
            return "auth/sign_up_page";
        }

        if (!viewModel.getPassword().equals(viewModel.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.no_match", "Password and confirm password do not match");
            logger.debug("Passwords don't match!");
            return "auth/sign_up_page";
        }

        if (errors.hasErrors()) {
            logger.debug("Errors");
            return "auth/sign_up_page";
        }

        logger.debug("Reached! before register!");

        try {
            service.createNewUser(viewModel.getUsername(), viewModel.getPassword());
            logger.debug("LogIn process");
            request.login(viewModel.getUsername(), viewModel.getPassword());
            logger.debug("Logged In");
        } catch (ServletException e) {
            logger.error("Error during auto login", e);
            return "auth/sign_up_page";
        }

        return "redirect:/";
    }


}
