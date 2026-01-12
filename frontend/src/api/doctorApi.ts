import { http } from './http';

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

export async function listDoctorsByHospital(hospitalId: number): Promise<DoctorResponse[]> {
  const { data } = await http.get<DoctorResponse[]>(`/doctors/hospital/${hospitalId}`);
  return data;
}

