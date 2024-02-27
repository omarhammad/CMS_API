# Clinic Management System

## Introduction

The Clinic Management System is a solution designed to automate and effectively handle a variety of operations in
appointment scheduling, medical records management, and prescription tracking.

## Features

### Appointments

- Schedule new appointments
- cancel existing appointments
- View appointment history

### Prescriptions

- Generate prescriptions electronically
- Track medication history

### Medical Records

- Patient under his details page he can see all his old appointments with a small description of what happened during
  that appointment.

## Domain Model

- Patient
- Doctor
- Appointment
- Prescription
- Medication

### Explanation :

> - Patient can be added with a national number and won't be added in case national number is in the system.
> - Doctor can be added, but they must have different contact info
> - Patient and Doctor can have multiple appointments but with different Date and Time.
> - Medications can be added normally but can't be deleted in case a Prescription using it.
> - Prescription can't be added without medications and the Expiry date must be in future and after the appointment
    date.
> - When removing a doctor or a patient their related appointments would be deleted also.
> - All appointments that in the past can't be deleted as they would be used as a medical record for Patients
> - when you open a Patient or Doctor details page , you'll see their appointments with who has been made
> - Patients additionally in their details page they can see a small description of each appointment they had in the
    past under Medical Records section

## Database

- Postgresql : everything to run the sql server in the docker-compose.yml file
- the host port is 5434

## Project Assignments

- ### Project - week 1
    - the project files of PROG-3 copied.
    - the project deal with SpringData only on Postgersql
    - docker-compose.yml file initiated with everything needed
    - host port 5434 mapped with container 5432 port to access the sql server

- ### Project - week 2
    - DoctorController has (GET ALL, GET ONE, POST, DELETE) endpoints
    - test.http file initiated in "test" package has tests for each endpoint and any potential status code.
    - a js file in statics folder in the resources package named "doctor.js" has functions that calls each endpoint using Ajax (fetch) and ready to be used. 

#### <span style ="color:orange">Thanks for reading my Project description.</span>

<div style="color: darkgreen">

### Made By :

#### Full name : Omar Yahya M Hammad

#### Group : ACS 201

#### Email : omar.hammad@student.kdg.be

</div>









