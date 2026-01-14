import { http } from './http';

export type HospitalResponse = {
  id: number;
  name: string;
  city: string;
  address: string;
  contactEmail: string;
  contactPhone: string;
  approved: boolean;
  active: boolean;
};

export async function listAllHospitals(): Promise<HospitalResponse[]> {
  const { data } = await http.get<HospitalResponse[]>('/hospitals?includeUnapproved=true');
  return data;
}

export async function approveHospital(hospitalId: number): Promise<void> {
  await http.put(`/hospitals/${hospitalId}/approve`);
}

export type CreateHospitalAdminRequest = {
  hospitalId: number;
  name: string;
  email: string;
  password: string;
  phone?: string;
  gender?: string;
  dateOfBirth?: string; // yyyy-MM-dd
};

export async function createHospitalAdmin(req: CreateHospitalAdminRequest): Promise<{ userId: number }> {
  const { data } = await http.post<{ userId: number }>('/admin/users/hospital-admin', req);
  return data;
}

export type HospitalAdminUserResponse = {
  userId: number;
  name: string;
  email: string;
  phone: string | null;
  gender: string | null;
  dateOfBirth: string | null;
  hospitalId: number | null;
  hospitalName: string | null;
  active: boolean;
};

export async function listHospitalAdmins(): Promise<HospitalAdminUserResponse[]> {
  const { data } = await http.get<HospitalAdminUserResponse[]>('/admin/users/hospital-admin');
  return data;
}

export async function setHospitalAdminActive(userId: number, value: boolean): Promise<HospitalAdminUserResponse> {
  const { data } = await http.put<HospitalAdminUserResponse>(`/admin/users/hospital-admin/${userId}/active?value=${value}`);
  return data;
}

export async function resetHospitalAdminPassword(userId: number, newPassword: string): Promise<void> {
  await http.put(`/admin/users/hospital-admin/${userId}/password`, { newPassword });
}
