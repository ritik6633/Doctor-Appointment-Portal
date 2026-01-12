import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { LoginPage } from '../auth/LoginPage';
import { RegisterPage } from '../auth/RegisterPage';
import { ProtectedRoute } from './ProtectedRoute';
import { DashboardLayout } from '../layouts/DashboardLayout';
import { PatientDashboard } from '../dashboards/patient/PatientDashboard';
import { BookAppointmentPage } from '../dashboards/patient/BookAppointmentPage';
import { MyAppointmentsPage } from '../dashboards/patient/MyAppointmentsPage';
import { DoctorDashboard } from '../dashboards/doctor/DoctorDashboard';
import { HospitalAdminDashboard } from '../dashboards/hospitalAdmin/HospitalAdminDashboard';
import { DeveloperAdminDashboard } from '../dashboards/developerAdmin/DeveloperAdminDashboard';

export function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<ProtectedRoute allowed={['PATIENT']} />}>
          <Route
            path="/patient/dashboard"
            element={
              <DashboardLayout>
                <PatientDashboard />
              </DashboardLayout>
            }
          />
          <Route
            path="/patient/book"
            element={
              <DashboardLayout>
                <BookAppointmentPage />
              </DashboardLayout>
            }
          />
          <Route
            path="/patient/appointments"
            element={
              <DashboardLayout>
                <MyAppointmentsPage />
              </DashboardLayout>
            }
          />
        </Route>

        <Route element={<ProtectedRoute allowed={['DOCTOR']} />}>
          <Route
            path="/doctor/dashboard"
            element={
              <DashboardLayout>
                <DoctorDashboard />
              </DashboardLayout>
            }
          />
        </Route>

        <Route element={<ProtectedRoute allowed={['HOSPITAL_ADMIN']} />}>
          <Route
            path="/hospital-admin/dashboard"
            element={
              <DashboardLayout>
                <HospitalAdminDashboard />
              </DashboardLayout>
            }
          />
        </Route>

        <Route element={<ProtectedRoute allowed={['DEVELOPER_ADMIN']} />}>
          <Route
            path="/developer-admin/dashboard"
            element={
              <DashboardLayout>
                <DeveloperAdminDashboard />
              </DashboardLayout>
            }
          />
        </Route>

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
