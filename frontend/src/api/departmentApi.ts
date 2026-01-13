import { http } from './http';

export type DepartmentResponse = {
  id: number;
  hospitalId: number;
  name: string;
  description: string | null;
};

export type CreateDepartmentRequest = {
  hospitalId: number;
  name: string;
  description?: string;
};

export async function createDepartment(req: CreateDepartmentRequest): Promise<DepartmentResponse> {
  const { data } = await http.post<DepartmentResponse>('/departments', req);
  return data;
}

export async function listDepartmentsByHospital(hospitalId: number): Promise<DepartmentResponse[]> {
  const { data } = await http.get<DepartmentResponse[]>(`/departments/hospital/${hospitalId}`);
  return data;
}
