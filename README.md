# Doctor Appointment Portal (Localhost)

A realistic **multi-hospital Doctor Appointment Portal** (mini SaaS healthcare system) built with **Spring Boot + MySQL + React (MUI)**.

This project is designed to be:
- **Interview-ready** (clean modules, DTOs, validations, service-layer authorization)
- **Industry-style** (multi-tenant hospitals, role dashboards, realistic workflows)
- **Fully runnable on localhost**

> Note on security (per your requirement): **No Spring Security / JWT / OAuth** is used. Authentication is intentionally simplified for demo by using request headers.

---

## Table of Contents

1. [Key Features](#key-features)
2. [Roles & Permissions](#roles--permissions)
3. [System Architecture](#system-architecture)
4. [Database Design](#database-design)
5. [Main User Journeys (Flows)](#main-user-journeys-flows)
6. [Local Setup](#local-setup)
7. [Run the Backend](#run-the-backend)
8. [Run the Frontend](#run-the-frontend)
9. [Authentication Model (Header-based)](#authentication-model-header-based)
10. [Seeded Accounts](#seeded-accounts)
11. [API Reference (Core)](#api-reference-core)
12. [Testing](#testing)
13. [Troubleshooting](#troubleshooting)

---

## Key Features

- **Multi-hospital support (multi-tenant)**
  - Hospitals are tenants: doctors/departments/appointments belong to a hospital.

- **4 roles with separate dashboards**
  - PATIENT, DOCTOR, HOSPITAL_ADMIN, DEVELOPER_ADMIN

- **Manual access control (service-layer checks)**
  - Each service method enforces role and ownership.

- **Slot-based booking**
  - Bookable slots come from weekly availability rules.
  - Prevents double booking.

- **Hospital approval + activation lifecycle**
  - Developer Admin must approve hospitals.
  - Developer Admin can deactivate hospitals.
  - Deactivated/unapproved hospitals are blocked from operational actions.

- **Reviews for doctors**
  - Patients can rate and comment.

---

## Roles & Permissions

### PATIENT
- Register & login
- View hospitals (approved + active only)
- Browse doctors by hospital/department
- View doctor available slots
- Book appointment (future slot only)
- Cancel appointment (only upcoming BOOKED)
- View appointment history
- Add doctor review

### DOCTOR
- View dashboard & today's schedule
- View assigned appointments
- Update appointment status (BOOKED → COMPLETED)
- Manage availability (restricted to self)

### HOSPITAL_ADMIN
- Manage only their hospital
- Create departments
- Add doctors
- Assign doctor availability
- View hospital appointment list / search
- Dashboard stats

### DEVELOPER_ADMIN
- Platform-level control
- View all hospitals
- Approve hospitals
- Activate / deactivate hospitals
- View platform dashboard stats
- Audit hospital appointments

---

## System Architecture

### Backend (Spring Boot)
**Controller → Service → Repository**

- Controllers: REST endpoints (no business logic)
- Services: business rules + authorization
- Repositories: Spring Data JPA persistence
- DTOs: requests/responses (no entity exposure)
- Validation: Hibernate Validator on request DTOs
- Exceptions: custom exceptions + global handler

**Module structure** (high level):

- `auth` – registration + login
- `hospital` – tenant lifecycle (create/approve/activate)
- `department` – hospital departments
- `doctor` – doctor creation + doctor dashboard + availability administration
- `availability` – slot generation for booking UI
- `appointment` – booking/cancel/status/search/audit
- `review` – patient reviews for doctors
- `dashboard` – role dashboard aggregates
- `common` – exception handling, config, utilities

### Frontend (React + TypeScript)
- Role-based routing with protected pages
- Layout per role (sidebar + top bar)
- Axios API layer
- Session stored in localStorage

---

## Database Design

Database name: **`spring`**

Schema + seeds are loaded automatically from:
- `src/main/resources/db/schema.sql`
- `src/main/resources/db/data.sql`

Core tables:
- `users` – all user accounts (patients, doctors, admins)
- `hospitals` – tenants
- `departments` – per hospital
- `doctors` – doctor profile (linked to user + hospital + department)
- `doctor_availability` – weekly rules used to generate slots
- `appointments` – booking records
- `reviews` – doctor feedback

---

## Main User Journeys (Flows)

### 1) Hospital Onboarding (Developer Admin)
1. Hospital is created (initially **approved=false**)
2. Developer Admin approves hospital
3. Hospital becomes visible to patients

### 2) Department + Doctor Setup (Hospital Admin)
1. Hospital Admin creates departments (for own hospital)
2. Hospital Admin creates doctors (creates a DOCTOR user + doctor profile)
3. Hospital Admin assigns weekly availability

### 3) Patient Booking Flow
1. Patient views approved+active hospitals
2. Select hospital → view doctors (optionally filter by department)
3. View doctor available slots (generated from weekly rules)
4. Book a future slot
5. System prevents double booking for same doctor + date + time

### 4) Doctor Appointment Flow
1. Doctor views today's appointments
2. Doctor updates status (BOOKED → COMPLETED)
3. Doctor cannot cancel patient appointment

### 5) Hospital Deactivation (Platform control)
If Developer Admin sets hospital to **inactive**:
- Patient booking is blocked
- Doctor operational actions are blocked
- Hospital Admin operational actions are blocked
- Developer Admin can still audit

---

## Local Setup

### Requirements
- Java **17**
- MySQL running on localhost
- Node.js 20.x (tested with Node 20.16)

### MySQL Configuration (your laptop)
- DB: `spring`
- username: `root`
- password: `root`

Create database:

```sql
CREATE DATABASE IF NOT EXISTS spring;
```

---

## Run the Backend

From repo root:

```powershell
cd D:\Spring\DoctorAppointment
.\mvnw.cmd spring-boot:run
```

Backend URL:
- http://localhost:8080

Health check:
- http://localhost:8080/actuator/health

---

## Run the Frontend

```powershell
cd D:\Spring\DoctorAppointment\frontend
npm install
npm run dev
```

Frontend URL:
- http://localhost:5174

---

## Authentication Model (Header-based)

Per requirements, authentication is simplified:

1. Client calls `POST /auth/login`
2. Server returns:
   - `userId`
   - `role`
   - `hospitalId` (nullable)
3. Frontend stores session and sends headers on every request:

Headers:
- `X-USER-ID: <userId>`
- `X-ROLE: <role>`

> This is NOT secure. It’s only for localhost demo to satisfy the “no Spring Security/JWT” requirement.

---

## Seeded Accounts

From `src/main/resources/db/data.sql`:

- Developer Admin
  - `devadmin@portal.com` / `Dev@123`

- Hospital Admin
  - `admin@citycare.com` / `Admin@123`

- Doctors
  - `rahul.sharma@citycare.com` / `Doc@123`
  - `priya.singh@citycare.com` / `Doc@123`

- Patients
  - `amit.patel@gmail.com` / `Patient@123`
  - `sneha.kapoor@gmail.com` / `Patient@123`

---

## API Reference (Core)

### Auth
- `POST /auth/register`
- `POST /auth/login`

### Hospitals
- `GET /hospitals` (public)
- `POST /hospitals`
- `PUT /hospitals/{id}/approve`
- `PUT /hospitals/{id}/active?value=true|false`

### Departments
- `POST /departments`
- `GET /departments/hospital/{id}`

### Doctors
- `POST /doctors`
- `GET /doctors/hospital/{id}`
  - optional: `?departmentId=<id>`
- `GET /doctors/me/dashboard`

### Availability
- `GET /availability/doctor/{id}?from=YYYY-MM-DD&days=7`
- `POST /availability` (upsert weekly rule)
- `GET /doctors/{doctorId}/availability`
- `PUT /doctors/{doctorId}/availability`

### Appointments
- `POST /appointments`
- `GET /appointments/patient/{id}`
- `GET /appointments/doctor/{id}`
- `GET /appointments/hospital/{id}`
- `GET /appointments/hospital/{id}/search?from=&to=&status=`
- `GET /appointments/hospital/{id}/audit` (developer admin)
- `PUT /appointments/{id}/cancel`
- `PUT /appointments/{id}/status`

### Reviews
- `POST /reviews`
- `GET /reviews/doctor/{doctorId}`

### Dashboards
- `GET /dashboard/patient`
- `GET /dashboard/doctor`
- `GET /dashboard/hospital-admin`
- `GET /dashboard/developer-admin`

---

## Testing

### Backend tests

```powershell
cd D:\Spring\DoctorAppointment
.\mvnw.cmd test
```

Includes:
- Service-level integration tests for booking / isolation / deactivation
- Full **controller tests** using `@WebMvcTest` for every controller

### Frontend checks

```powershell
cd D:\Spring\DoctorAppointment\frontend
npm run build
```

---

## Troubleshooting

### 1) MySQL connection error
Confirm:
- MySQL is running
- DB exists: `spring`
- Credentials match `application.properties`

### 2) Port already in use
- Backend default: 8080
- Frontend default: 5174

Stop the process using the port or change configuration.

### 3) Booking says "Doctor is not available"
Common reasons:
- Availability rule not created for that day
- Time not aligned with slotDuration
- Hospital is inactive/unapproved
- Doctor is inactive

---

## Project Notes

- Uses Java 17 + Spring Boot **3.3.x** for stable testing support.
- No token auth by design (requirement), but service-layer authorization is enforced.
- DTO-based API (no entity leakage) + global exception handler.
