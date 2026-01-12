import React, { createContext, useContext, useMemo, useState } from 'react';
import type { AuthState, LoginResponse } from '../types/auth';
import { clearAuth, loadAuth, saveAuth } from './authStorage';

interface AuthContextValue {
  auth: AuthState | null;
  login: (login: LoginResponse) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [auth, setAuth] = useState<AuthState | null>(() => loadAuth());

  const value = useMemo<AuthContextValue>(
    () => ({
      auth,
      login: (loginRes) => setAuth(saveAuth(loginRes)),
      logout: () => {
        clearAuth();
        setAuth(null);
      },
    }),
    [auth],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider');
  return ctx;
}

