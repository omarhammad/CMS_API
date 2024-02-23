package com.example.clinicmanagementsystem.domain;

import java.time.LocalDateTime;

public class SessionHistory {
    private LocalDateTime sessionDateTime;
    private String pageVisited;


    public SessionHistory(LocalDateTime sessionDateTime, String pageVisited) {
        this.sessionDateTime = sessionDateTime;
        this.pageVisited = pageVisited;
    }

    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public String getPageVisited() {
        return pageVisited;
    }

    public void setPageVisited(String pageVisited) {
        this.pageVisited = pageVisited;
    }


    @Override
    public String toString() {
        return "\nSessionHistory{" +
                "sessionDateTime=" + sessionDateTime +
                ", pageVisited='" + pageVisited + '\'' +
                '}';
    }
}
