ALTER TABLE appointments
    ALTER CONSTRAINT fkdcu22gag6dshn4gm1lhta0bpv DEFERRABLE INITIALLY DEFERRED;
BEGIN;
SET CONSTRAINTS ALL DEFERRED;
INSERT INTO stakeholder (first_name, last_name, username, contact_info, role, password)
VALUES ('Omar', 'Johnson', 'omar_johnson', '+32123456789,omar.johnson@email.com', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Sara', 'Lee', 'sara_lee', '+32234567890,sara.lee@email.com', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Kevin', 'Miller', 'kevin_miller', '+32345678901,kevin.miller@email.com', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Nora', 'Davis', 'nora_davis', '+32456789012,nora.davis@email.com', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Felix', 'Brown', 'felix_brown', '+32567890123,felix.brown@email.com', 'DOCTOR',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe');


INSERT INTO doctors (id, specialization)
VALUES (1, 'Cardiology'),
       (2, 'Neurology'),
       (3, 'Orthopedics'),
       (4, 'Pediatrics'),
       (5, 'Dermatology');

INSERT INTO stakeholder (first_name, last_name, username, contact_info, password, role)
VALUES ('Lily', 'Smith', 'lily_smith', '+32567890543,lily.smith@email.com',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT'),
       ('Max', 'Taylor', 'max_taylor', '+32567890443,max.taylor@email.com',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT'),
       ('Emma', 'Jones', 'emma_jones', '+32567394123,emma.jones@email.com',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT'),
       ('Noah', 'Anderson', 'noah_andreson', '+32018890123,noah.andreson@email.com',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'PATIENT');

INSERT INTO patients (id, age, gender, national_number)
VALUES (6, 30, 'F', '91.07.23-543.21'),
       (7, 25, 'M', '98.02.11-654.32'),
       (8, 36, 'F', '87.03.15-321.98'),
       (9, 29, 'M', '94.08.26-432.76');

INSERT INTO stakeholder (first_name, last_name, contact_info, username, password, role)
VALUES ('Mahmoud', 'Hammad', '+32018890123,mahmoud.hammad@email.com', 'secretary',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'SECRETARY'),
       ('Omar', 'Hammad', '+32018890123,omar.hammad@email.com', 'admin',
        '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe', 'ADMIN');


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