import { Alert, Box, Button, ButtonGroup, Container, Divider, Stack, TextField, Typography } from '@mui/material';
import { useMemo, useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { login } from '../api/authApi';
import { useAuth } from './AuthContext';
import { GlassCard } from '../components/GlassCard';
import type { Role } from '../types/auth';

const rolePresets: Record<Role, { title: string; hint: string; demoEmail: string }> = {
  PATIENT: { title: 'Patient Login', hint: 'Book and manage appointments', demoEmail: 'amit.patel@gmail.com' },
  DOCTOR: { title: 'Doctor Login', hint: 'Manage schedule and appointments', demoEmail: 'rahul.sharma@citycare.com' },
  HOSPITAL_ADMIN: { title: 'Hospital Admin Login', hint: 'Manage doctors & departments', demoEmail: 'admin@citycare.com' },
  DEVELOPER_ADMIN: { title: 'Developer Admin Login', hint: 'Approve hospitals and platform controls', demoEmail: 'devadmin@portal.com' },
};

function labelForRole(r: Role) {
  switch (r) {
    case 'PATIENT':
      return 'Patient';
    case 'DOCTOR':
      return 'Doctor';
    case 'HOSPITAL_ADMIN':
      return 'Hospital Admin';
    case 'DEVELOPER_ADMIN':
      return 'Developer Admin';
  }
}

export function LoginPage() {
  const [selectedRole, setSelectedRole] = useState<Role>('PATIENT');
  const [email, setEmail] = useState(rolePresets.PATIENT.demoEmail);
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);

  const { login: setAuthLogin } = useAuth();
  const navigate = useNavigate();

  const selected = useMemo(() => rolePresets[selectedRole], [selectedRole]);

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: { xs: 4, md: 10 } }}>
        <GlassCard>
          <Stack spacing={2}>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 900, letterSpacing: 0.2 }}>
                {selected.title}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {selected.hint}
              </Typography>
            </Box>

            <ButtonGroup fullWidth variant="outlined">
              {(['PATIENT', 'DOCTOR', 'HOSPITAL_ADMIN', 'DEVELOPER_ADMIN'] as Role[]).map((r) => (
                <Button
                  key={r}
                  variant={selectedRole === r ? 'contained' : 'outlined'}
                  onClick={() => {
                    setSelectedRole(r);
                    setEmail(rolePresets[r].demoEmail);
                    setError(null);
                  }}
                >
                  {labelForRole(r)}
                </Button>
              ))}
            </ButtonGroup>

            {error && (
              <Alert severity="error" variant="outlined">
                {error}
              </Alert>
            )}

            <Box
              component="form"
              onSubmit={async (e) => {
                e.preventDefault();
                setError(null);
                try {
                  const res = await login({ email, password });

                  // Strict portal enforcement (industry UX)
                  if (res.role !== selectedRole) {
                    setError(`This account is a ${labelForRole(res.role)} account. Please use the ${labelForRole(res.role)} portal.`);
                    return;
                  }

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
              <Stack spacing={2}>
                <TextField
                  fullWidth
                  label="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  autoComplete="email"
                />
                <TextField
                  fullWidth
                  label="Password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  autoComplete="current-password"
                />

                <Button type="submit" variant="contained" size="large">
                  Sign in
                </Button>

                <Divider />

                <Button component={RouterLink} to="/register" variant="text" disabled={selectedRole !== 'PATIENT'}>
                  Create a patient account
                </Button>

                {selectedRole !== 'PATIENT' && (
                  <Typography variant="caption" color="text.secondary">
                    Patient registration is self-serve. Doctor accounts are created by Hospital Admin. Hospital Admin accounts are created by Developer Admin.
                  </Typography>
                )}
              </Stack>
            </Box>
          </Stack>
        </GlassCard>
      </Box>
    </Container>
  );
}
