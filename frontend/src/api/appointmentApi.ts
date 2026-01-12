import { http } from './http';

export type AppointmentStatus = 'BOOKED' | 'CANCELLED' | 'COMPLETED';

export type AppointmentResponse = {
  id: number;
  doctorId: number;
  doctorName: string;
  patientId: number;
  patientName: string;
  hospitalId: number;
  appointmentDate: string;
  appointmentTime: string;
  status: AppointmentStatus;
  symptoms: string;
};

export type BookAppointmentRequest = {
  doctorId: number;
  appointmentDate: string; // yyyy-MM-dd
  appointmentTime: string; // HH:mm:ss
  symptoms: string;
};

export async function bookAppointment(req: BookAppointmentRequest): Promise<AppointmentResponse> {
  const { data } = await http.post<AppointmentResponse>('/appointments', req);
  return data;
}

export async function listPatientAppointments(patientId: number): Promise<AppointmentResponse[]> {
  const { data } = await http.get<AppointmentResponse[]>(`/appointments/patient/${patientId}`);
  return data;
}

export async function cancelAppointment(appointmentId: number): Promise<AppointmentResponse> {
  const { data } = await http.put<AppointmentResponse>(`/appointments/${appointmentId}/cancel`);
  return data;
}

