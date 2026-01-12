# Doctor Appointment Portal (Multi-Hospital)

This repository contains the backend (Spring Boot, Java 17, MySQL) and frontend (React + TypeScript + MUI) for a multi-role Doctor Appointment Portal.

## Prerequisites
- Java 17 (Temurin recommended)
- MySQL 8+
- Node.js 18+

## Backend runs with Java 17 even if your machine defaults to Java 8
Your machine currently defaults to Java 8, so use the provided helper script for backend commands:

```powershell
cd D:\Spring\DoctorAppointment
powershell -NoProfile -ExecutionPolicy Bypass -File .\run-local.ps1 test
powershell -NoProfile -ExecutionPolicy Bypass -File .\run-local.ps1 spring
```

## Database
Update `src/main/resources/application.properties` with your MySQL DB name/user/pass.

## Frontend
Frontend will live in `frontend/` (React + TS + MUI) and will call the backend on `http://localhost:8080`.

---

> Next: full backend modules and React dashboards will be generated under the required package structure: `com.doctorportal.*`.

