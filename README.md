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
    - DoctorController has (GET ALL, GET ONE, DELETE) endpoints
    - test.http file initiated in "test" package has tests for each endpoint and any potential status code.
    - js files in statics/js/doctors folder called {get_doctors.js "has the delete request",get_one_doctor.js}, in each
      js file i call the endpoints to fill in each page using Ajax (
      fetch).
    - > GET http://localhost:8080/api/doctors :  "Fetching all doctors : Response is OK"
    - > GET http://localhost:8080/api/doctors :  "Fetching all doctors : Response is NO_CONTENT (EMPTY LIST)"
    - > GET http://localhost:8080/api/doctors/1 :  "Fetching one doctor : Response is OK"
    - > GET http://localhost:8080/api/doctors/20 :  "Fetching one doctor : Response is NOT FOUND"
    - > DELETE http://localhost:8080/api/doctors/1 :  "Deleting one doctor : Response is NO_CONTENT (DELETED!)"
    - > DELETE http://localhost:8080/api/doctors/20 :  "Deleting one doctor : Response is NOT_FOUND"


- ### Project - week 3
    - DoctorController hsa (POST,PUT) endpoints
    - test.http file handles all status codes for PUT,POST requests.
    - post_doctor.js & put_doctor.js files are in static/js/doctors folder to make the request using "fetch" and handel
      add_new_doctor.html & update_doctor_page.hml pages.
    - > POST http://localhost:8080/api/doctors :  "Posting a new doctor: Response is OK"
      >> Content-Type: application/json
      >
      >> {
      "firstName": "Omar",
      "lastName": "Hammad",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }
    - > POST http://localhost:8080/api/doctors :  "Posting a new doctor: Response is Bad Request as fields are not
      valid!"
      >> Content-Type: application/json
      >
      >> {
      "firstName": " ",
      "lastName": " ",
      "specialization": " ",
      "phoneNumber": " ",
      "email": " "
      }
    - > POST http://localhost:8080/api/doctors :  "Posting a new doctor: Response is Bad Request as the contact already"
      exists!"
      >> Content-Type: application/json
      >
      >> {
      "firstName": "Omar",
      "lastName": "Hammad",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }
    - > PUT http://localhost:8080/api/doctors/1 :  "Updating a doctor: Response is NO_CONTENT (UPDATED!)"
      >> Content-Type: application/json
      >
      >> {
      "id": 1,
      "firstName": "Omar",
      "lastName": "Hammad",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }

    - > PUT http://localhost:8080/api/doctors/2 :  "Updating a doctor: Response is CONFLICT"
      >> Content-Type: application/json
      >
      >> {
      "id": 1,
      "firstName": "Omar",
      "lastName": "Hammad",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }

    - > PUT http://localhost:8080/api/doctors/1 :  "Updating a doctor: Response is Bad Request as fields are not
      valid!"
      >> Content-Type: application/json
      >
      >> {
      "id": 1,
      "firstName": " ",
      "lastName": " ",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }
    - > PUT http://localhost:8080/api/doctors/2 :  "Updating a doctor: Response is BAD REQUEST DUE TO CONTACT INFO USED
      BY OTHER DOCTORS"
      >> Content-Type: application/json
      >
      >> {
      "id": 2,
      "firstName": "Hasan",
      "lastName": "Alkhatib",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }

    - > PUT http://localhost:8080/api/doctors/20 : "Updating a doctor: Response is DOCTOR NOT FOUND"
      >> Content-Type: application/json
      >
      >> {
      "id": 20,
      "firstName": "Hasan",
      "lastName": "Alkhatib",
      "specialization": "Urology",
      "phoneNumber": "+32465358794",
      "email": "omar.hammad@student.kd.be"
      }
---

- ### Project - week 4
    - I've created a signIn and signUp pages also a Landing page.
    - Users already in the system :
        - username: "sara_lee" with Password "omar1997"
        - username: "kevin_miller" with Password "omar1997"
        - username: "lily_smith" with Password "omar1997"
        - username: "admin" with Password "omar1997"
    - SignIn page (/signin) , SignUp(/signup) and Landing page (/) can be accessed by anyone.
    - Appointments (/appointments) , Patients (/patients) , Doctors (/doctors) and Medications (/medications)  requires
      authentication to be accessible.

Certainly! Here's a revised version of your project description suitable for inclusion in a README file, with corrections for grammar and vocabulary:

---

## Project - Week 5

### Overview

This application incorporates a role-based access control system with four distinct roles: Patient, Doctor, Secretary, and Admin. Each role is associated with specific permissions that define the actions users can perform within the application.

### User Roles and Credentials

All users share the same password: `omar1997`. Below are the usernames assigned to each role:

- **Doctor**: Username - `sara_lee`
- **Patient**: Username - `lily_smith`
- **Admin**: Username - `admin`
- **Secretary**: Username - `secretary`

### Role Permissions

- **Doctors** are able to:
    - View only their own appointments.
    - Add, delete, and update medications.
    - View other doctors' profiles.
    - Add and update patient information.
    - Access a profile tab to update their personal information.

- **Patients** are permitted to:
    - View only their own appointments.
    - View medications and other doctors' profiles.
    - Access a profile tab to update their personal information.

- **Secretaries** can:
    - Schedule appointments and view all appointments.
    - Add new patients to the system.
    - View doctors' profiles and their scheduled appointments.
    - Secretaries do not have access to medication details.

- **Admins** have full access to:
    - Perform all actions within the application without restrictions.
---

For your Week 6 project update intended for a README file, here's a polished and concise version:

---

## Project Update - Week 6

### Current Progress:

- Development has primarily focused on implementing critical functionalities within the application. A notable milestone is the creation of the `AppointmentRestControllerTest` class, designed to validate key functionalities of the appointment management system.

### Testing Strategy:

The `AppointmentRestControllerTest` employs Spring Boot's testing framework to ensure the reliability and integrity of appointment-related operations. Here's a glimpse into the testing setup and methodologies:

#### Technologies and Tools:
- **Spring Boot Test**: Facilitates integration testing with auto-configuration and application context management.
- **AutoConfigureMockMvc**: Enables testing of Spring MVC controllers without starting a full HTTP server.
- **MockMvc**: Provides a powerful API for testing MVC controllers by simulating HTTP requests and asserting responses.

#### Test Implementation:
- **Setup Phase**: Prepares the testing environment by adding sample patients and doctors to the system, ensuring a realistic scenario for appointment functionalities.
- **Testing Appointment Creation**: Verifies the system's ability to handle new appointment requests correctly, including successful creation and handling of duplicate requests.

#### Key Test Cases:
1. **Creating New Appointments**: Confirms that new appointments can be added to the system, asserting a `201 Created` response for successful operations.
2. **Handling Duplicate Appointments**: Ensures the system correctly identifies and rejects duplicate appointment requests, returning a `400 Bad Request` response.

### Future Directions:

- **Comprehensive Component Testing**: While the current focus has been on appointment management, plans are underway to expand testing coverage to other components of the application, enhancing overall robustness and reliability.
---


#### <span style ="color:orange">Thanks for reading my Project description.</span>

<div style="color: darkgreen">

### Made By :

#### Full name : Omar Yahya M Hammad

#### Group : ACS 201

#### Email : omar.hammad@student.kdg.be

</div>









