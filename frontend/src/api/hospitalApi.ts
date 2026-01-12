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

export async function listHospitals(): Promise<HospitalResponse[]> {
  const { data } = await http.get<HospitalResponse[]>('/hospitals');
  return data;
}
