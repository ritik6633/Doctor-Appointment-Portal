import type { AuthState, LoginResponse } from '../types/auth';

const KEY = 'doctor_portal_auth';

export function loadAuth(): AuthState | null {
  const raw = localStorage.getItem(KEY);
  if (!raw) return null;
  try {
    const parsed = JSON.parse(raw) as AuthState;
    if (!parsed?.userId || !parsed?.role) return null;
    return parsed;
  } catch {
    return null;
  }
}

export function saveAuth(login: LoginResponse): AuthState {
  const state: AuthState = { ...login, isAuthenticated: true };
  localStorage.setItem(KEY, JSON.stringify(state));
  return state;
}

export function clearAuth() {
  localStorage.removeItem(KEY);
}

