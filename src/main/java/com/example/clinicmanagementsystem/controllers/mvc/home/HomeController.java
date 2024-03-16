package com.example.clinicmanagementsystem.controllers.mvc.home;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
@RequestMapping("")
public class HomeController {


    private static final String LANG = "en";
    private final Logger logger;

    private final LocaleResolver localeResolver;

    public HomeController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
        logger = LoggerFactory.getLogger(HomeController.class);
    }

    @GetMapping("/")
    public String root(HttpServletRequest request, HttpServletResponse response) {

        // Set locale to Dutch
        Locale locale = new Locale(LANG);
        this.localeResolver.setLocale(request, response, locale);
        logger.debug("Current Locale: {}", localeResolver.resolveLocale(request));
        return "home/home";
    }

}
