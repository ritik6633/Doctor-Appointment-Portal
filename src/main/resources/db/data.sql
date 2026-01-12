-- Doctor Appointment Portal - Seed Data
-- Password hashes use BCrypt. Plaintext noted for local dev.

-- Hospitals
INSERT INTO hospitals (id, name, city, address, contact_email, contact_phone, approved, active)
VALUES
  (1, 'CityCare Hospital', 'Pune', 'MG Road, Pune', 'admin@citycare.com', '9999991111', TRUE, TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Departments
INSERT INTO departments (id, hospital_id, name, description)
VALUES
  (1, 1, 'Cardiology', 'Heart and vascular care'),
  (2, 1, 'Dermatology', 'Skin and hair care')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Users
-- Developer Admin (password: Dev@123)
INSERT INTO users (id, name, email, password, role, phone, gender, date_of_birth, hospital_id, active)
VALUES
  (1, 'Developer Admin', 'devadmin@portal.com', '$2a$10$CMoQeS8cMbYlHqosjKpK6e3m0kUT7wAHXHkGv0b9Q7X5fHgdg8y02', 'DEVELOPER_ADMIN', '9000000001', 'MALE', '1995-01-01', NULL, TRUE)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Hospital Admin for CityCare (password: Admin@123)
INSERT INTO users (id, name, email, password, role, phone, gender, date_of_birth, hospital_id, active)
VALUES
  (2, 'Hospital Admin', 'admin@citycare.com', '$2a$10$0wX3n2lSll8lLW7u2fAq8eu8xV3kVt2lRz1pGz6y5g4M4LRf6f0Ku', 'HOSPITAL_ADMIN', '9000000002', 'FEMALE', '1992-02-02', 1, TRUE)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Doctors (password: Doc@123)
INSERT INTO users (id, name, email, password, role, phone, gender, date_of_birth, hospital_id, active)
VALUES
  (3, 'Dr. Rahul Sharma', 'rahul.sharma@citycare.com', '$2a$10$8u8vGkQq3EwJ/eJ3u7n2PejVqzqkq2M.0QAXw7p6pWq0gXlBq2x3G', 'DOCTOR', '9000000003', 'MALE', '1988-03-03', 1, TRUE),
  (4, 'Dr. Neha Singh', 'neha.singh@citycare.com', '$2a$10$8u8vGkQq3EwJ/eJ3u7n2PejVqzqkq2M.0QAXw7p6pWq0gXlBq2x3G', 'DOCTOR', '9000000004', 'FEMALE', '1987-04-04', 1, TRUE)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Patients (password: Patient@123)
INSERT INTO users (id, name, email, password, role, phone, gender, date_of_birth, hospital_id, active)
VALUES
  (5, 'Amit Patel', 'amit.patel@gmail.com', '$2a$10$JjG8pJt9c2xwR8Gq6v0s1eK3x2uXGq0xHfZq3PpZb9hQv0m7QzqQK', 'PATIENT', '9000000005', 'MALE', '1999-05-05', NULL, TRUE),
  (6, 'Priya Desai', 'priya.desai@gmail.com', '$2a$10$JjG8pJt9c2xwR8Gq6v0s1eK3x2uXGq0xHfZq3PpZb9hQv0m7QzqQK', 'PATIENT', '9000000006', 'FEMALE', '2000-06-06', NULL, TRUE)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Doctors table
INSERT INTO doctors (id, user_id, hospital_id, department_id, specialization, qualification, experience_years, consultation_fee, active)
VALUES
  (1, 3, 1, 1, 'Cardiologist', 'MBBS, MD', 10, 800.00, TRUE),
  (2, 4, 1, 2, 'Dermatologist', 'MBBS, DDVL', 8, 600.00, TRUE)
ON DUPLICATE KEY UPDATE specialization=VALUES(specialization);

-- Doctor availability
INSERT INTO doctor_availability (id, doctor_id, day_of_week, start_time, end_time, slot_duration_minutes)
VALUES
  (1, 1, 'MONDAY', '10:00:00', '13:00:00', 15),
  (2, 1, 'WEDNESDAY', '10:00:00', '13:00:00', 15),
  (3, 2, 'TUESDAY', '16:00:00', '19:00:00', 15),
  (4, 2, 'THURSDAY', '16:00:00', '19:00:00', 15)
ON DUPLICATE KEY UPDATE day_of_week=VALUES(day_of_week);

-- Sample appointment
INSERT INTO appointments (id, doctor_id, patient_id, hospital_id, appointment_date, appointment_time, status, symptoms)
VALUES
  (1, 1, 5, 1, CURDATE(), '10:15:00', 'BOOKED', 'Chest discomfort')
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- Sample review
INSERT INTO reviews (id, doctor_id, patient_id, rating, comment)
VALUES
  (1, 1, 5, 5, 'Very professional and explained everything clearly.')
ON DUPLICATE KEY UPDATE rating=VALUES(rating);

