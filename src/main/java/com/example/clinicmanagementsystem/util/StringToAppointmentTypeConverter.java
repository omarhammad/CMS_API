package com.example.clinicmanagementsystem.util;

import com.example.clinicmanagementsystem.domain.util.AppointmentType;
import org.springframework.core.convert.converter.Converter;

public class StringToAppointmentTypeConverter implements Converter<String, AppointmentType> {
    @Override
    public AppointmentType convert(String source) {
        if (source.toLowerCase().startsWith("con")) return AppointmentType.CONSULTATION;
        if (source.toLowerCase().startsWith("fol")) return AppointmentType.FOLLOW_UP;
        if (source.toLowerCase().startsWith("eme")) return AppointmentType.EMERGENCY;
        if (source.toLowerCase().startsWith("diag")) return AppointmentType.DIAGNOSTIC_TESTING;

        return AppointmentType.CONSULTATION;
    }
}
