package com.example.clinicmanagementsystem.services.csvServices;

import com.example.clinicmanagementsystem.domain.Medication;
import com.example.clinicmanagementsystem.repository.medicationsRepo.MedicationsSpringData;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class CsvService implements ICsvService {

    private final MedicationsSpringData medicationsRepo;

    public CsvService(MedicationsSpringData medicationsRepo) {
        this.medicationsRepo = medicationsRepo;
    }


    @Override
    public void processMedicationsCsv(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CsvToBean<Medication> csvToBean = new CsvToBeanBuilder<Medication>(reader)
                    .withType(Medication.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Medication> medications = csvToBean.parse();
            medicationsRepo.saveAll(medications);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
