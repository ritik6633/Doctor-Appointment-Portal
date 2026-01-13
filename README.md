# Doctor Appointment Portal (Localhost)

Full-stack Doctor Appointment Portal with **multi-hospital** support and **role-based dashboards**.

## Tech Stack

- Backend: Java 17, Spring Boot, Spring Data JPA, MySQL, Lombok, Hibernate Validator, BCrypt
- Frontend: React + TypeScript + Material UI + Axios + React Router
- Auth: Basic header-based auth (no Spring Security / JWT for now)

## Local Requirements

- Java 17
- MySQL running on localhost
- Node.js 20.x (project tested with Node 20.16)

## Database Setup

Create database:

```sql
CREATE DATABASE IF NOT EXISTS spring;
```

Credentials (as per your laptop config):

- username: `root`
- password: `root`
- database: `spring`

The backend loads schema + sample data automatically from:

- `src/main/resources/db/schema.sql`
- `src/main/resources/db/data.sql`

## Run Backend (Spring Boot)

From repo root:

```powershell
cd D:\Spring\DoctorAppointment
.\mvnw.cmd spring-boot:run
```

Backend runs on:
- http://localhost:8080

## Run Frontend (React)

```powershell
cd D:\Spring\DoctorAppointment\frontend
npm install
npm run dev
```

Frontend runs on:
- http://localhost:5174

## Authentication (Important)

This project uses **basic authentication logic** via request headers.

When you login, frontend stores:
- `userId`
- `role`
- `hospitalId` (nullable)

Axios automatically sends headers:

- `X-USER-ID: <userId>`
- `X-ROLE: <role>`

## Seeded Login Accounts

From `data.sql`:

- Developer Admin
  - `devadmin@portal.com` / `Dev@123`
- Hospital Admin
  - `admin@citycare.com` / `Admin@123`
- Doctor
  - `rahul.sharma@citycare.com` / `Doc@123`
- Patient
  - `amit.patel@gmail.com` / `Patient@123`

## Role Routes

- `/login`
- `/register`

- Patient
  - `/patient/dashboard`
  - `/patient/book`
  - `/patient/appointments`
  - `/patient/reviews`

- Doctor
  - `/doctor/dashboard`
  - `/doctor/appointments`

- Hospital Admin
  - `/hospital-admin/dashboard`
  - `/hospital-admin/departments`
  - `/hospital-admin/doctors`
  - `/hospital-admin/availability`

- Developer Admin
  - `/developer-admin/dashboard`
  - `/developer-admin/hospitals`

## API Summary

Auth:
- `POST /auth/register`
- `POST /auth/login`

Hospitals:
- `GET /hospitals`
- `POST /hospitals`
- `PUT /hospitals/{id}/approve`

Departments:
- `POST /departments`
- `GET /departments/hospital/{id}`

Doctors:
- `POST /doctors`
- `GET /doctors/hospital/{id}`
- `GET /doctors/{doctorId}/availability`
- `PUT /doctors/{doctorId}/availability`

Appointments:
- `POST /appointments`
- `GET /appointments/patient/{id}`
- `GET /appointments/doctor/{id}`
- `PUT /appointments/{id}/cancel`
- `PUT /appointments/{id}/status`

Reviews:
- `POST /reviews`
- `GET /reviews/doctor/{doctorId}`

Dashboards:
- `GET /dashboard/patient`
- `GET /dashboard/doctor`
- `GET /dashboard/hospital-admin`
- `GET /dashboard/developer-admin`

## Quality Gates

- Backend: `mvn test`
- Frontend: `npm run build`

