package com.example.clinicmanagementsystem.configs;

import com.example.clinicmanagementsystem.domain.util.Unit;
import org.springframework.core.convert.converter.Converter;


public class StringToUnitConverter implements Converter<String, Unit> {
    @Override
    public Unit convert(String source) {
        if (source.equalsIgnoreCase("milliliters")) {
            return Unit.ML;
        }
        return Unit.MG;
    }
}
