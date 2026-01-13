import { http } from './http';

export type CreateDoctorRequest = {
  hospitalId: number;
  departmentId: number;
  name: string;
  email: string;
  password: string;
  phone?: string;
  gender?: string;
  dateOfBirth?: string; // yyyy-MM-dd
  specialization: string;
  qualification?: string;
  experienceYears?: number;
  consultationFee: number;
};

export type DoctorResponse = {
  id: number;
  userId: number;
  name: string;
  email: string;
  hospitalId: number;
  departmentId: number;
  departmentName: string;
  specialization: string;
  qualification: string | null;
  experienceYears: number | null;
  consultationFee: number;
  active: boolean;
};

export async function createDoctor(req: CreateDoctorRequest): Promise<DoctorResponse> {
  const { data } = await http.post<DoctorResponse>('/doctors', req);
  return data;
}

export type UpsertAvailabilityRequest = {
  dayOfWeek:
    | 'MONDAY'
    | 'TUESDAY'
    | 'WEDNESDAY'
    | 'THURSDAY'
    | 'FRIDAY'
    | 'SATURDAY'
    | 'SUNDAY';
  startTime: string; // HH:mm:ss
  endTime: string; // HH:mm:ss
  slotDurationMinutes: number;
};

export type DoctorAvailabilityResponse = {
  id: number;
  doctorId: number;
  dayOfWeek: UpsertAvailabilityRequest['dayOfWeek'];
  startTime: string;
  endTime: string;
  slotDurationMinutes: number;
};

export async function upsertDoctorAvailability(
  doctorId: number,
  req: UpsertAvailabilityRequest,
): Promise<DoctorAvailabilityResponse> {
  const { data } = await http.put<DoctorAvailabilityResponse>(`/doctors/${doctorId}/availability`, req);
  return data;
}

