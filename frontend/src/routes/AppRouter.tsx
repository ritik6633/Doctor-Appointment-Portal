import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { LoginPage } from '../auth/LoginPage';
import { RegisterPage } from '../auth/RegisterPage';
import { ProtectedRoute } from './ProtectedRoute';
import { DashboardLayout } from '../layouts/DashboardLayout';
import { PatientDashboard } from '../dashboards/patient/PatientDashboard';
import { BookAppointmentPage } from '../dashboards/patient/BookAppointmentPage';
import { MyAppointmentsPage } from '../dashboards/patient/MyAppointmentsPage';
import { DoctorReviewsPage } from '../dashboards/patient/DoctorReviewsPage';
import { DoctorDashboard } from '../dashboards/doctor/DoctorDashboard';
import { MyDoctorAppointmentsPage } from '../dashboards/doctor/MyDoctorAppointmentsPage';
import { HospitalAdminDashboard } from '../dashboards/hospitalAdmin/HospitalAdminDashboard';
import { DeveloperAdminDashboard } from '../dashboards/developerAdmin/DeveloperAdminDashboard';
import { ManageDepartmentsPage } from '../dashboards/hospitalAdmin/ManageDepartmentsPage';
import { AddDoctorPage } from '../dashboards/hospitalAdmin/AddDoctorPage';
import { ManageAvailabilityPage } from '../dashboards/hospitalAdmin/ManageAvailabilityPage';
import { ManageHospitalsPage } from '../dashboards/developerAdmin/ManageHospitalsPage';
import { CreateHospitalAdminPage } from '../dashboards/developerAdmin/CreateHospitalAdminPage';
import { ManageHospitalAdminsPage } from '../dashboards/developerAdmin/ManageHospitalAdminsPage';

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
          <Route
            path="/patient/reviews"
            element={
              <DashboardLayout>
                <DoctorReviewsPage />
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
          <Route
            path="/doctor/appointments"
            element={
              <DashboardLayout>
                <MyDoctorAppointmentsPage />
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
          <Route
            path="/hospital-admin/departments"
            element={
              <DashboardLayout>
                <ManageDepartmentsPage />
              </DashboardLayout>
            }
          />
          <Route
            path="/hospital-admin/doctors"
            element={
              <DashboardLayout>
                <AddDoctorPage />
              </DashboardLayout>
            }
          />
          <Route
            path="/hospital-admin/availability"
            element={
              <DashboardLayout>
                <ManageAvailabilityPage />
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
          <Route
            path="/developer-admin/hospitals"
            element={
              <DashboardLayout>
                <ManageHospitalsPage />
              </DashboardLayout>
            }
          />
          <Route
            path="/developer-admin/hospital-admins/create"
            element={
              <DashboardLayout>
                <CreateHospitalAdminPage />
              </DashboardLayout>
            }
          />
          <Route
            path="/developer-admin/hospital-admins"
            element={
              <DashboardLayout>
                <ManageHospitalAdminsPage />
              </DashboardLayout>
            }
          />
        </Route>

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
