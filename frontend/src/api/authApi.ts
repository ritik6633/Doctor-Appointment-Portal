import { http } from './http';
import type { LoginResponse } from '../types/auth';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone?: string;
  gender?: string;
  dateOfBirth: string; // yyyy-MM-dd
}

export async function login(req: LoginRequest): Promise<LoginResponse> {
  const { data } = await http.post<LoginResponse>('/auth/login', req);
  return data;
}

export async function register(req: RegisterRequest): Promise<{ userId: number }> {
  const { data } = await http.post<{ userId: number }>('/auth/register', req);
  return data;
}
