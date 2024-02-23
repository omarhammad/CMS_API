package com.example.clinicmanagementsystem.domain.util;

public enum MedicationForm {
    Tablets("Solid form, often swallowed or allowed to dissolve in the mouth."),

    Capsules("Gelatin shells filled with powder or liquid medication."),

    Liquids("Solutions, suspensions, or syrups taken orally."),

    Topicals("Creams, ointments, gels, and patches applied to the skin."),

    Inhalers("Devices delivering medication to the lungs, often for asthma or COPD."),

    Injections(" Liquid medication given by needle, including intravenous (IV), intramuscular (IM), and subcutaneous (SC) routes."),

    Suppositories(" Solid medication inserted into the rectum, vagina, or urethra."),

    Drops("Used for the eyes, ears, or nose."),

    Powders("Often mixed with a liquid before taking."),

    Implants(" Placed under the skin for long-term medication release.");

    private String usage;

    MedicationForm(String usage) {
        this.usage = usage;
    }


    public String getUsage() {
        return usage;
    }
}
