INSERT INTO doctors (first_name, last_name, specialization, contact_info)
VALUES ('Omar', 'Johnson', 'Cardiology', '+32123456789,omar.johnson@email.com');

INSERT INTO doctors (first_name, last_name, specialization, contact_info)
VALUES ('Sara', 'Lee', 'Neurology', '+32234567890,sara.lee@email.com');

INSERT INTO doctors (first_name, last_name, specialization, contact_info)
VALUES ('Kevin', 'Miller', 'Orthopedics', '+32345678901,kevin.miller@email.com');

INSERT INTO doctors (first_name, last_name, specialization, contact_info)
VALUES ('Nora', 'Davis', 'Pediatrics', '+32456789012,nora.davis@email.com');

INSERT INTO doctors (first_name, last_name, specialization, contact_info)
VALUES ('Felix', 'Brown', 'Dermatology', '+32567890123,felix.brown@email.com');



INSERT INTO patients (first_name, last_name, age, gender, national_number)
VALUES ('Omar', 'Williams', 42, 'M', '78.05.12-234.56');
INSERT INTO patients (first_name, last_name, age, gender, national_number)
VALUES ('Lily', 'Smith', 30, 'F', '91.07.23-543.21');
INSERT INTO patients (first_name, last_name, age, gender, national_number)
VALUES ('Max', 'Taylor', 25, 'M', '98.02.11-654.32');
INSERT INTO patients (first_name, last_name, age, gender, national_number)
VALUES ('Emma', 'Jones', 36, 'F', '87.03.15-321.98');
INSERT INTO patients (first_name, last_name, age, gender, national_number)
VALUES ('Noah', 'Anderson', 29, 'M', '94.08.26-432.76');


INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type)
VALUES ('2023-01-15 10:00:00', 'Routine Checkup', 1, 1, 'CONSULTATION');
INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type)
VALUES ('2023-01-20 14:30:00', 'Follow-up', 2, 2, 'FOLLOW_UP');
INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type)
VALUES ('2023-02-05 09:00:00', 'Annual Physical', 3, 3, 'DIAGNOSTIC_TESTING');
INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type)
VALUES ('2023-02-10 16:00:00', 'Consultation for Symptoms', 4, 4, 'CONSULTATION');
INSERT INTO appointments (appointment_date_time, purpose, doctor_id, patient_id, appointment_type)
VALUES ('2023-02-18 11:30:00', 'Emergency Visit', 5, 5, 'EMERGENCY');


INSERT INTO medications (name, form, dosageUnit, dosageQuantity, frequencies, daysDuration, notes)
VALUES ('MedicationA', 'Tablets', 'MG', 500, 2, 14, 'Take after meals');
INSERT INTO medications (name, form, dosageUnit, dosageQuantity, frequencies, daysDuration, notes)
VALUES ('MedicationB', 'Liquids', 'ML', 10, 3, 7, 'Shake well before use');
INSERT INTO medications (name, form, dosageUnit, dosageQuantity, frequencies, daysDuration, notes)
VALUES ('MedicationC', 'Tablets', 'MG', 250, 1, 10, 'Take before bedtime');
INSERT INTO medications (name, form, dosageUnit, dosageQuantity, frequencies, daysDuration, notes)
VALUES ('MedicationD', 'Capsules', 'MG', 100, 4, 5, 'With plenty of water');
INSERT INTO medications (name, form, dosageUnit, dosageQuantity, frequencies, daysDuration, notes)
VALUES ('MedicationE', 'Injections', 'ML', 1, 1, 3, 'Administered by healthcare professional');


INSERT INTO prescriptions (expireDate)
VALUES ('2023-06-30');
INSERT INTO prescriptions (expireDate)
VALUES ('2023-07-15');
INSERT INTO prescriptions (expireDate)
VALUES ('2023-08-20');
INSERT INTO prescriptions (expireDate)
VALUES ('2023-09-10');
INSERT INTO prescriptions (expireDate)
VALUES ('2023-10-05');


INSERT INTO prescription_medications (prescriptionId, medicationId)
VALUES (1, 1);
INSERT INTO prescription_medications (prescriptionId, medicationId)
VALUES (2, 2);
INSERT INTO prescription_medications (prescriptionId, medicationId)
VALUES (3, 3);
INSERT INTO prescription_medications (prescriptionId, medicationId)
VALUES (4, 4);
INSERT INTO prescription_medications (prescriptionId, medicationId)
VALUES (5, 5);