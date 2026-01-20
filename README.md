# Doctor Appointment Portal (Localhost)

A realistic **multi-hospital Doctor Appointment Portal** (mini SaaS healthcare system) built with **Spring Boot (Java 17) + MySQL + React (TypeScript + MUI)**.

This repository is intentionally designed to look and behave like a **production-style** healthcare product (simpler than Practo, but architecture-driven). It’s suitable for:
- Interview demos (**multi-module backend, DTOs, validations, service-layer authorization**)
- Learning clean architecture (Controller → Service → Repository)
- A strong “real project” portfolio piece

> **Security note (per requirement):** This project does **NOT** use Spring Security, JWT, OAuth, or sessions.
> Authentication is intentionally simplified for localhost demo using **login + role headers**.

---

## Contents

1. [What you get](#what-you-get)
2. [Roles and permissions](#roles-and-permissions)
3. [Key workflows (end-to-end)](#key-workflows-end-to-end)
4. [Technology stack](#technology-stack)
5. [Backend architecture](#backend-architecture)
6. [Database design](#database-design)
7. [API + auth model (header-based)](#api--auth-model-header-based)
8. [Seeded accounts (ready-to-login)](#seeded-accounts-ready-to-login)
9. [Run on Windows (Java 17 + MySQL + Node)](#run-on-windows-java-17--mysql--node)
10. [Commands cheat-sheet](#commands-cheat-sheet)
11. [Testing](#testing)
12. [Troubleshooting](#troubleshooting)

---

## What you get

### Product-grade features
- **Multi-hospital (multi-tenant) platform**
  - Hospitals are tenants.
  - Doctors / departments / appointments belong to a hospital.

- **Role-based dashboards and routes**
  - PATIENT, DOCTOR, HOSPITAL_ADMIN, DEVELOPER_ADMIN

- **Manual authorization (service-layer checks)**
  - Every sensitive operation validates role + ownership.

- **Availability → slots → booking**
  - Weekly availability rules generate bookable slots.
  - Prevents double booking (doctor/date/time).

- **Hospital approval lifecycle**
  - Developer Admin must approve a hospital before it appears to patients.
  - Developer Admin can activate/deactivate hospitals.

- **Reviews**
  - Patients can rate/comment on doctors.

---

## Roles and permissions

### PATIENT
- Register & login
- View hospitals (**approved + active only**)
- Browse doctors by hospital/department
- View doctor available slots
- Book appointment (future slots only)
- Cancel appointment (only upcoming BOOKED)
- View appointment history
- Add review for doctor

### DOCTOR
- View doctor dashboard (today’s schedule)
- View assigned appointments
- Update appointment status (BOOKED → COMPLETED)
- Manage availability (restricted to self)

### HOSPITAL_ADMIN
- Manage only their hospital
- Create departments
- Add doctors (creates DOCTOR user + doctor profile)
- Assign doctor availability
- View hospital appointments / search
- Hospital dashboard stats

### DEVELOPER_ADMIN
- Platform-level control
- View all hospitals
- Approve hospitals
- Activate/deactivate hospitals
- Platform dashboard stats
- Audit hospital appointments

---

## Key workflows (end-to-end)

### 1) Hospital onboarding (Developer Admin)
1. Hospital is created (starts as `approved=false`)
2. Developer Admin approves hospital
3. Hospital becomes visible to patients (if `active=true` as well)

### 2) Hospital setup (Hospital Admin)
1. Hospital Admin creates departments (for their hospital)
2. Hospital Admin creates doctors
3. Hospital Admin assigns weekly availability rules

### 3) Patient booking flow
1. Patient views approved+active hospitals
2. Select hospital → view doctors (optional department filter)
3. View doctor available slots for date range
4. Book a future slot
5. System blocks double booking

### 4) Doctor daily operations
1. Doctor views today’s appointments
2. Doctor updates appointment status to COMPLETED

### 5) Platform control / deactivation
If Developer Admin deactivates a hospital:
- Patient booking is blocked
- Doctor operational actions are blocked
- Hospital admin operational actions are blocked
- Developer admin can still audit

---

## Technology stack

### Backend
- Java **17**
- Spring Boot
- Spring Data JPA
- MySQL
- Lombok
- Hibernate Validator
- BCrypt password hashing
- REST APIs

### Frontend
- React + TypeScript
- Material UI (MUI)
- Axios
- React Router DOM
- Responsive layout

---

## Backend architecture

### Layering rules
**Controller → Service → Repository**

- Controllers
  - Only HTTP mapping + request validation
  - No business rules
- Services
  - Business logic
  - Manual authorization checks (role + ownership)
  - Transaction boundaries when required
- Repositories
  - JPA persistence
- DTOs everywhere
  - No entity exposure through APIs

### Module structure

```
com.doctorportal
 ├── auth
 ├── user
 ├── hospital
 ├── department
 ├── doctor
 ├── appointment
 ├── availability
 ├── review
 ├── dashboard
 ├── common
     ├── config
     ├── exception
     └── util
```

---

## Database design

Database name: **`spring`**

Schema + seeds are loaded automatically from:
- `src/main/resources/db/schema.sql`
- `src/main/resources/db/data.sql`

### Core tables
- `users` – all users (patients, doctors, admins)
- `hospitals` – tenants
- `departments` – belongs to hospital
- `doctors` – doctor profile (linked to user + hospital + department)
- `doctor_availability` – weekly time rules
- `appointments` – booking records
- `reviews` – feedback

---

## API + auth model (header-based)

### Why headers?
Per requirement: **no Spring Security + no JWT**.
So this project uses a simple demo model:

1. Client calls `POST /auth/login`
2. Server returns:
   - `userId`
   - `role`
   - `hospitalId` (nullable)
3. Frontend stores session and sends headers with each request:

Headers:
- `X-USER-ID: <userId>`
- `X-ROLE: <role>`

> This is not secure and is only intended for localhost demonstration.

---

## Seeded accounts (ready-to-login)

Seed data is in `src/main/resources/db/data.sql`.

### Developer Admin
- Email: `devadmin@portal.com`
- Password: `Dev@123`

### Hospital Admin
- Email: `admin@citycare.com`
- Password: `Admin@123`

### Doctors
- Email: `rahul.sharma@citycare.com`
- Password: `Doc@123`

- Email: `priya.singh@citycare.com`
- Password: `Doc@123`

### Patients
- Email: `amit.patel@gmail.com`
- Password: `Patient@123`

- Email: `sneha.kapoor@gmail.com`
- Password: `Patient@123`

---

## Run on Windows (Java 17 + MySQL + Node)

### Prerequisites
- Java **17** installed and selected in PATH
- MySQL running
- Node.js 20.x (recommended)

### 1) Create database

```sql
CREATE DATABASE IF NOT EXISTS spring;
```

### 2) Configure application properties
For your machine:
- `spring.datasource.url` should point to DB `spring`
- username: `root`
- password: `root`

### 3) Run backend

```powershell
Set-Location "D:\Spring\DoctorAppointment"
.\mvnw.cmd spring-boot:run
```

Backend:
- http://localhost:8080

Health endpoint:
- http://localhost:8080/actuator/health

### 4) Run frontend

```powershell
Set-Location "D:\Spring\DoctorAppointment\frontend"
npm install
npm run dev
```

Frontend:
- http://localhost:5174

---

## Commands cheat-sheet

### IMPORTANT: PowerShell `cd /d` is CMD syntax
In **PowerShell**, use `Set-Location` (or `cd` without `/d`).

✅ Correct:
```powershell
Set-Location "D:\Spring\DoctorAppointment"
```

❌ Incorrect (CMD-only):
```powershell
cd /d D:\Spring\DoctorAppointment
```

### View log tail (PowerShell)
```powershell
Get-Content .\frontend\build.log -Tail 80
```

---

## Testing

### Backend tests

```powershell
Set-Location "D:\Spring\DoctorAppointment"
.\mvnw.cmd test
```

Includes:
- Service-level tests for booking validation / tenant isolation / hospital activation checks
- Controller tests (`@WebMvcTest`) for core APIs

### Frontend build check

```powershell
Set-Location "D:\Spring\DoctorAppointment\frontend"
npm run build
```

---

## Troubleshooting

### 1) Maven is using Java 8 (but you need Java 17)
If `mvn -v` shows Java 1.8, set JAVA_HOME to JDK 17 and restart the terminal.

Example check:
```powershell
java -version
.\mvnw.cmd -v
```

### 2) Vite/Babel error: missing `@babel/parser`
This typically occurs when node_modules installation is corrupted.
Fix:
```powershell
Set-Location "D:\Spring\DoctorAppointment\frontend"
Remove-Item -Recurse -Force node_modules
Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
npm install
npm run dev
```

### 3) Port already in use
- Backend: 8080
- Frontend: 5174

Stop the process using the port or change configuration.

### 4) Booking says “Doctor is not available”
Common reasons:
- Availability rules not created for that weekday
- Time not aligned with slot duration
- Hospital inactive/unapproved
- Doctor inactive

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
- `GET /doctors/hospital/{id}` (optional query: `departmentId`)
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

