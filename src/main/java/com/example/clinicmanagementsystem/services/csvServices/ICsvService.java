package com.example.clinicmanagementsystem.services.csvServices;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ICsvService {

    void processMedicationsCsv(InputStream csvFile);
}
