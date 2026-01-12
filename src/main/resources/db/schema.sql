-- Doctor Appointment Portal - MySQL Schema
-- Database: spring
--
-- IMPORTANT:
-- 1) Keep this file non-empty. Spring's SQL initializer will fail if it is empty.
-- 2) This schema follows your required table design.

DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS doctor_availability;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS hospitals;

CREATE TABLE hospitals (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(120) NOT NULL,
  address VARCHAR(500) NOT NULL,
  contact_email VARCHAR(255) NOT NULL,
  contact_phone VARCHAR(30) NOT NULL,
  approved BOOLEAN NOT NULL DEFAULT FALSE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(40) NOT NULL,
  phone VARCHAR(30),
  gender VARCHAR(20),
  date_of_birth DATE,
  hospital_id BIGINT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_users_hospital FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE departments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  hospital_id BIGINT NOT NULL,
  name VARCHAR(120) NOT NULL,
  description VARCHAR(500),
  CONSTRAINT fk_departments_hospital FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE doctors (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  hospital_id BIGINT NOT NULL,
  department_id BIGINT NOT NULL,
  specialization VARCHAR(255) NOT NULL,
  qualification VARCHAR(255),
  experience_years INT,
  consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_doctors_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_doctors_hospital FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
  CONSTRAINT fk_doctors_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE doctor_availability (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  doctor_id BIGINT NOT NULL,
  day_of_week VARCHAR(20) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  slot_duration_minutes INT NOT NULL,
  CONSTRAINT fk_doctor_availability_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE appointments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  doctor_id BIGINT NOT NULL,
  patient_id BIGINT NOT NULL,
  hospital_id BIGINT NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time TIME NOT NULL,
  status VARCHAR(30) NOT NULL,
  symptoms VARCHAR(1000),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES users(id),
  CONSTRAINT fk_appointments_hospital FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  doctor_id BIGINT NOT NULL,
  patient_id BIGINT NOT NULL,
  rating INT NOT NULL,
  comment VARCHAR(1000),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reviews_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_reviews_patient FOREIGN KEY (patient_id) REFERENCES users(id)
);

CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_hospital ON users(hospital_id);
CREATE INDEX idx_doctors_hospital ON doctors(hospital_id);
CREATE INDEX idx_doctors_department ON doctors(department_id);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX idx_appointments_hospital ON appointments(hospital_id);
