# Clinic Management System

![Clinic Management System](https://github.com/omarhammad/CMS_WebApp/blob/master/src/main/resources/static/media/landing_page.png)

---

## Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [Domain Model](#domain-model)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Security and Authentication](#security-and-authentication)
- [Contributors](#contributors)

---

## Overview

The **Clinic Management System** is a web-based application designed to streamline clinic operations such as appointment scheduling, medical record management, and prescription tracking. It ensures seamless communication between doctors, patients, and administrative staff while maintaining data integrity and security.

---

## Key Features

### Appointment Management
- Schedule, update, and cancel appointments.
- View appointment history.

### Prescription Management
- Generate electronic prescriptions.
- Track medication history.

### Medical Records
- Patients can view their previous appointments and treatment notes.

### User Roles and Permissions
- **Admin**: Full access to manage doctors, patients, and appointments.
- **Doctor**: Can view, update, and manage their own appointments and prescribe medications.
- **Patient**: Can book and view their own appointments and medical records.
- **Secretary**: Can schedule and manage appointments for patients.

---

## Domain Model

- **Patient**: Unique national number-based identification.
- **Doctor**: Unique contact details.
- **Appointment**: Patient-Doctor relation with a date and time.
- **Prescription**: Contains medication details, linked to an appointment.
- **Medication**: Can be prescribed but not deleted if in use.

---

## Technologies Used

- **Backend**: Java Spring Boot (Spring Data JPA, Spring Security)
- **Frontend**: Embedded JavaScript, Bootstrap, SCSS
- **Database**: PostgreSQL (Dockerized)
- **Caching**: Implemented for search operations
- **Testing**: JUnit, Mockito for unit and integration testing
- **Authentication**: Role-based access control (RBAC) with Spring Security

---

## Setup and Installation

### Prerequisites
- Java 17+
- Docker & Docker Compose
- PostgreSQL (if running locally)

### Installation Steps

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/clinic-management.git
   cd clinic-management
   ```
2. Run PostgreSQL with Docker:
   ```sh
   docker-compose up -d
   ```
3. Configure environment variables:
   ```sh
   cp .env.example .env
   ```
4. Build and run the project:
   ```sh
   ./gradlew bootRun
   ```
5. Access the application at `http://localhost:8080`

---

## API Endpoints

### Doctor Endpoints
- `GET /api/doctors` - Retrieve all doctors
- `GET /api/doctors/{id}` - Get a specific doctor
- `POST /api/doctors` - Add a new doctor
- `PUT /api/doctors/{id}` - Update a doctor
- `DELETE /api/doctors/{id}` - Remove a doctor

### Appointment Endpoints
- `GET /api/appointments` - Retrieve all appointments
- `POST /api/appointments` - Schedule a new appointment
- `DELETE /api/appointments/{id}` - Cancel an appointment

### Patient Endpoints
- `GET /api/patients/{id}` - Retrieve patient details
- `POST /api/patients` - Register a new patient

---

## Testing

### Unit & Integration Testing
- **Mockito**: Used for mocking repository dependencies.
- **JUnit**: Ensures correct functionality of service and controller layers.
- **API Tests**: Covers all endpoints for CRUD operations.

Example Test Case:
```java
@Test
void shouldReturnAllAppointments() {
    List<Appointment> appointments = List.of(new Appointment(1L, "2024-01-29", "Checkup"));
    given(appointmentsRepo.findAll()).willReturn(appointments);
    assertEquals(1, appointmentSvc.getAllAppointments().size());
}
```

---

## Security and Authentication

- **Role-Based Access Control (RBAC)** ensures users can only access relevant features.
- **CSRF Protection** enabled for form submissions.
- **Password Encryption** implemented using BCrypt.

---


## Contributors

👨‍💻 **Omar Yahya M Hammad**  
📧 Email: [omar.hammad@student.kdg.be](mailto:omar.hammad@student.kdg.be)
