export type Role = 'PATIENT' | 'DOCTOR' | 'HOSPITAL_ADMIN' | 'DEVELOPER_ADMIN';

export interface LoginResponse {
  userId: number;
  role: Role;
  hospitalId: number | null;
}

export interface AuthState extends LoginResponse {
  isAuthenticated: boolean;
}

