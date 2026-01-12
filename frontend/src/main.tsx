import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { CssBaseline } from '@mui/material';
import './index.css';
import { AuthProvider } from './auth/AuthContext';
import { AppRouter } from './routes/AppRouter';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AuthProvider>
      <CssBaseline />
      <AppRouter />
    </AuthProvider>
  </StrictMode>,
);
