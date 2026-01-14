import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { CssBaseline } from '@mui/material';
import { ThemeProvider } from '@mui/material/styles';
import './index.css';
import { AuthProvider } from './auth/AuthContext';
import { AppRouter } from './routes/AppRouter';
import { appTheme } from './theme';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ThemeProvider theme={appTheme}>
      <AuthProvider>
        <CssBaseline />
        <AppRouter />
      </AuthProvider>
    </ThemeProvider>
  </StrictMode>,
);
