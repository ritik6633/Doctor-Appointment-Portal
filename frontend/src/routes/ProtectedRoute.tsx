import { Navigate, Outlet } from 'react-router-dom';
import type { Role } from '../types/auth';
import { useAuth } from '../auth/AuthContext';

export function ProtectedRoute({ allowed }: { allowed: Role[] }) {
  const { auth } = useAuth();

  if (!auth?.isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!allowed.includes(auth.role)) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}

