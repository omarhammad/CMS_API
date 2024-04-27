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

## Project - Week 6 & 7


### `AppointmentSvcTest` - Service Layer Testing

This class tests various functionalities provided by the `AppointmentSvc` service layer, focusing on the CRUD operations and retrieval of appointment data.

#### Features Tested

1. **Retrieving All Appointments**
    - Tests that the list of all appointments is not empty.

2. **Retrieving an Individual Appointment**
    - Validates the retrieval of an existing appointment.
    - Ensures that retrieving a non-existent appointment returns null.

3. **Adding a New Appointment**
    - Tests the addition of a new appointment.
    - Verifies that the newly added appointment matches the expected values by comparing it with a mapped DTO of the appointment object.

4. **Updating an Appointment**
    - Confirms that an existing appointment can be updated.
    - Checks if the update operation correctly modifies the appointment details, such as the purpose of the visit.

5. **Removing an Appointment**
    - Ensures that an appointment can be deleted.
    - Verifies that once an appointment is removed, it can no longer be retrieved.

6. **Retrieving Appointments for a Specific Patient**
    - Tests retrieval of all appointments for a given patient.
    - Confirms that all retrieved appointments belong to the specified patient.

7. **Retrieving Appointments for a Specific Doctor**
    - Ensures that all appointments for a given doctor are correctly retrieved.
    - Checks that each appointment's doctor ID matches the expected doctor.

### `AppointmentRestControllerTest` - Controller Layer Testing

This class tests the RESTful endpoints related to appointment management, simulating various scenarios to validate the appointment API's reliability and security.

#### Features Tested

1. **Creating a New Appointment**
    - Tests endpoint for adding a new appointment.
    - Validates response when trying to add the same appointment twice.
    - Checks handling of invalid input such as missing or incorrect patient national number, appointments in the past, and invalid doctor IDs.

2. **Retrieving Appointments by Patient and Doctor**
    - Tests endpoint for retrieving appointments for a specific patient, including scenarios where the patient has no appointments or does not exist.
    - Checks the retrieval of appointments for a doctor, handling cases where the doctor has no appointments or does not exist in the system.

3. **Deleting an Appointment**
    - Tests endpoint for deleting an appointment.
    - Validates the response when trying to delete a non-existent appointment.

#### Additional Scenarios

- **Security and Access Control**
    - Tests are conducted under the assumption that the user performing the operations has administrative rights (`WithUserDetails("admin")`).
    - Includes CSRF protection to test the security aspect of POST and DELETE requests.

----

## Project - week 8

#### Mockito Library and Mocking in Tests

In this project, I utilized the Mockito library extensively to facilitate the isolation of the `AppointmentSvc` during testing by mocking dependencies like `AppointmentSpringData` and `StakeholdersSpringData` repos .
##### Examples of Mockito Usage

1. **Given**: This is used to define the behavior of the mock when certain conditions are met. For example:
   ```java
   // Mocking the behavior to return an optional of appointment when a specific ID is searched
  
   // Given
   Appointment appointment = new Appointment();
   appointment.setId(1L);
   given(appointmentsRepo.findById(1L)).willReturn(Optional.of(appointment));
   
   // When
   AppointemntResponseDTO appointmentResponse = 
                appointmentSvc.getAppointmentById(1L);

   // Then   
   assertEquals(modelMapper.map(appointment,AppointemntResponseDTO.class)
                                           ,appointmentResponse);

   
   ```

2. **Verify**: This is used to ensure that specific interactions with the mock happen. For example:
   ```java
   // Verifying that the deleteById method was called on the repository
  
   // Given
   doNothing().when(appointmentsRepo).deleteById(1L);
   // When
   appointmentSvc.removeAppointment(1L);
   // Then
   verify(appointmentsRepo).deleteById(1L);

This command will execute all the test cases in the `AppointmentSvcTest` class, utilizing Mockito for mocking the necessary dependencies and verifying the interactions and behaviors as expected.

---

#### <span style ="color:orange">Thanks for reading my Project description.</span>

<div style="color: darkgreen">

### Made By :

#### Full name : Omar Yahya M Hammad

#### Group : ACS 201

#### Email : omar.hammad@student.kdg.be

</div>









