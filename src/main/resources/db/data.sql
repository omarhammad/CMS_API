ALTER TABLE appointments
    ALTER CONSTRAINT fkdcu22gag6dshn4gm1lhta0bpv DEFERRABLE INITIALLY DEFERRED;
BEGIN;
SET CONSTRAINTS ALL DEFERRED;
INSERT INTO stakeholder (first_name, last_name, username, role, password)
VALUES ('Omar', 'Johnson', 'omar_johnson', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Sara', 'Lee', 'sara_lee', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Kevin', 'Miller', 'kevin_miller', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Nora', 'Davis', 'nora_davis', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Felix', 'Brown', 'felix_brown', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe');


INSERT INTO doctors (id, specialization, contact_info)
VALUES (1, 'Cardiology', '+32123456789,omar.johnson@email.com'),
       (2, 'Neurology', '+32234567890,sara.lee@email.com'),
       (3, 'Orthopedics', '+32345678901,kevin.miller@email.com'),
       (4, 'Pediatrics', '+32456789012,nora.davis@email.com'),
       (5, 'Dermatology', '+32567890123,felix.brown@email.com');

INSERT INTO stakeholder (first_name, last_name, username, password, role)
VALUES ('Lily', 'Smith', 'lily_smith', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT'),
       ('Max', 'Taylor', 'max_taylor', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT'),
       ('Emma', 'Jones', 'emma_jones', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT'),
       ('Noah', 'Anderson', 'noah_andreson', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT');

INSERT INTO patients (id, age, gender, national_number)
VALUES (6, 30, 'F', '91.07.23-543.21'),
       (7, 25, 'M', '98.02.11-654.32'),
       (8, 36, 'F', '87.03.15-321.98'),
       (9, 29, 'M', '94.08.26-432.76');

INSERT INTO stakeholder (first_name, last_name, username, password, role)
VALUES ('Mahmoud', 'Hammad', 'secretary', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'SECRETARY'),
       ('Omar', 'Hammad', 'admin', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'ADMIN');


INSERT INTO availability(slot, used, doctor_id)
VALUES ('2024-05-20 12:00 PM', TRUE, 1),
       ('2024-06-20 11:00 PM', TRUE, 2),
       ('2024-06-20 11:30 PM', TRUE, 3),
       ('2024-06-20 12:00 PM', TRUE, 4),
       ('2024-06-20 01:00 PM', TRUE, 1),
       ('2024-06-20 02:00 PM', FALSE, 1),
       ('2024-06-20 01:00 PM', FALSE, 2),
       ('2024-06-20 02:00 PM', FALSE, 2),
       ('2024-06-20 01:00 PM', FALSE, 3),
       ('2024-06-20 02:00 PM', FALSE, 3),
       ('2024-06-20 01:00 PM', FALSE, 4),
       ('2024-06-20 02:00 PM', FALSE, 4);

INSERT INTO appointments (slot_id, purpose, doctor_id, patient_id, appointment_type, prescription_id)
VALUES (1, 'Routine Checkup', 1, 6, 'CONSULTATION', 1),
       (2, 'Follow-up', 2, 7, 'FOLLOW_UP', 2),
       (3, 'Annual Physical', 3, 8, 'DIAGNOSTIC_TESTING', 3),
       (4, 'Consultation for Symptoms', 4, 9, 'CONSULTATION', 4),
       (5, 'Consultation for Symptoms', 1, 9, 'CONSULTATION', null);

INSERT INTO medications (name, form, unit, quantity, frequencies, days_duration, notes)
VALUES ('MedicationA', 'Tablets', 'MG', 500, 2, 14, 'Take after meals'),
       ('MedicationB', 'Liquids', 'ML', 10, 3, 7, 'Shake well before use'),
       ('MedicationC', 'Tablets', 'MG', 250, 1, 10, 'Take before bedtime'),
       ('MedicationD', 'Capsules', 'MG', 100, 4, 5, 'With plenty of water'),
       ('MedicationE', 'Injections', 'ML', 1, 1, 3, 'Administered by healthcare professional'),
       ('MedicationF', 'Injections', 'ML', 1, 1, 3, 'Administered by healthcare professional');


INSERT INTO prescriptions (expire_date)
VALUES ('2023-06-30'),
       ('2023-07-15'),
       ('2023-08-20'),
       ('2023-09-10'),
       ('2023-10-05');


INSERT INTO prescriptions_medications (prescription_prescription_id, medications_medication_id)
VALUES (1, 1),
       (2, 2),
       (2, 1),
       (3, 3),
       (4, 4),
       (5, 5);





COMMIT;