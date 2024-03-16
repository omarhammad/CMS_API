ALTER TABLE appointments
    ALTER CONSTRAINT fkdcu22gag6dshn4gm1lhta0bpv DEFERRABLE INITIALLY DEFERRED;
BEGIN;
SET CONSTRAINTS ALL DEFERRED;
INSERT INTO stakeholder (first_name, last_name)
VALUES ('Omar', 'Johnson'),
       ('Sara', 'Lee'),
       ('Kevin', 'Miller'),
       ('Nora', 'Davis'),
       ('Felix', 'Brown');


INSERT INTO doctors (id, specialization, contact_info)
VALUES (1, 'Cardiology', '+32123456789,omar.johnson@email.com'),
       (2, 'Neurology', '+32234567890,sara.lee@email.com'),
       (3, 'Orthopedics', '+32345678901,kevin.miller@email.com'),
       (4, 'Pediatrics', '+32456789012,nora.davis@email.com'),
       (5, 'Dermatology', '+32567890123,felix.brown@email.com');

INSERT INTO stakeholder (first_name, last_name)
VALUES ('Lily', 'Smith'),
       ('Max', 'Taylor'),
       ('Emma', 'Jones'),
       ('Noah', 'Anderson');

INSERT INTO patients (id, age, gender, national_number)
VALUES (6, 30, 'F', '91.07.23-543.21'),
       (7, 25, 'M', '98.02.11-654.32'),
       (8, 36, 'F', '87.03.15-321.98'),
       (9, 29, 'M', '94.08.26-432.76');



INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type, prescription_id)
VALUES ('2023-01-15 10:00:00', 'Routine Checkup', 1, 6, 'CONSULTATION', 1),
       ('2023-01-20 14:30:00', 'Follow-up', 2, 7, 'FOLLOW_UP', 2),
       ('2023-02-05 09:00:00', 'Annual Physical', 3, 8, 'DIAGNOSTIC_TESTING', 3),
       ('2024-03-20 16:00:00', 'Consultation for Symptoms', 4, 9, 'CONSULTATION', 4);
INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type)
VALUES ('2023-02-18 11:30:00', 'Emergency Visit', 5, 6, 'EMERGENCY');

INSERT INTO medications (name, form, unit, quantity, frequencies, days_duration, notes)
VALUES ('MedicationA', 'Tablets', 'MG', 500, 2, 14, 'Take after meals'),
       ('MedicationB', 'Liquids', 'ML', 10, 3, 7, 'Shake well before use'),
       ('MedicationC', 'Tablets', 'MG', 250, 1, 10, 'Take before bedtime'),
       ('MedicationD', 'Capsules', 'MG', 100, 4, 5, 'With plenty of water'),
       ('MedicationE', 'Injections', 'ML', 1, 1, 3, 'Administered by healthcare professional');


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


INSERT INTO application_user(username, password)
VALUES ('Omar', '$2a$10$l7OaNsKv6s9FkonaKSdMquYrRvF1kWLC51w.I62RM4yFkfpdLjNGe'),
       ('Fadi', '$2a$10$da5a0Bpqa3wv6yLT23iFAeTji6tJnCgEDrTi/7KLxsqhte1YKkLGK'),
       ('Mahmoud', '$2a$10$zs6z57nTK/.BjZcogkrlrugPDZ/Yx1SJ3lDOmvzdfrFT1CtenA51m'),
       ('Othman', '$2a$10$c6VXz0xGaZDz9wG/HtlauulwBU1Yp.gH16faNjruOe/uz9eaFeKqu');

COMMIT;