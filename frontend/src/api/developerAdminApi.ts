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
  const { data } = await http.get<HospitalResponse[]>('/hospitals');
  return data;
}

export async function approveHospital(hospitalId: number): Promise<void> {
  await http.put(`/hospitals/${hospitalId}/approve`);
}

