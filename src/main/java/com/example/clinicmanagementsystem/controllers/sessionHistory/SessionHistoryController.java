package com.example.clinicmanagementsystem.controllers.sessionHistory;


import com.example.clinicmanagementsystem.domain.SessionHistory;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/sessions")
public class SessionHistoryController {


    Logger logger;

    public SessionHistoryController() {
        this.logger = LoggerFactory.getLogger(SessionHistoryController.class);

    }

    @GetMapping("/")
    public String getSessionsHistoryPage(Model model, HttpSession session) {
        logger.debug("Loading Session History Page");
        if (session.getAttribute("sessionHistory") == null) {
            session.setAttribute("sessionHistory", new ArrayList<SessionHistory>());
        }
        ArrayList<SessionHistory> sessionHistories =
                (ArrayList<SessionHistory>) session.getAttribute("sessionHistory");
        model.addAttribute("sessionHistories", sessionHistories);
        return "sessions/sessionHistoryDetails";
    }
}
