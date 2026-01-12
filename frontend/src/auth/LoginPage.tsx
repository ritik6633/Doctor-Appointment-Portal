import { Box, Button, Container, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { login } from '../api/authApi';
import { useAuth } from './AuthContext';

export function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);

  const { login: setAuthLogin } = useAuth();
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 8 }}>
        <Typography variant="h4" sx={{ mb: 2 }}>
          Login
        </Typography>

        <Box component="form"
          onSubmit={async (e) => {
            e.preventDefault();
            setError(null);
            try {
              const res = await login({ email, password });
              setAuthLogin(res);
              const role = res.role;
              if (role === 'PATIENT') navigate('/patient/dashboard');
              else if (role === 'DOCTOR') navigate('/doctor/dashboard');
              else if (role === 'HOSPITAL_ADMIN') navigate('/hospital-admin/dashboard');
              else navigate('/developer-admin/dashboard');
            } catch (err: any) {
              setError(err?.response?.data?.message ?? 'Login failed');
            }
          }}
        >
          <TextField
            fullWidth
            label="Email"
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            fullWidth
            label="Password"
            type="password"
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          {error && (
            <Typography color="error" variant="body2" sx={{ mt: 1 }}>
              {error}
            </Typography>
          )}

          <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
            Login
          </Button>

          <Button component={RouterLink} to="/register" fullWidth sx={{ mt: 1 }}>
            Create an account
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

