import { Alert, Box, Button, Container, Stack, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { register } from '../api/authApi';
import { GlassCard } from '../components/GlassCard';

export function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [dateOfBirth, setDob] = useState('2000-01-01');
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: { xs: 4, md: 10 } }}>
        <GlassCard>
          <Stack spacing={2}>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 900, letterSpacing: 0.2 }}>
                Patient Registration
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Create a patient account. Doctor and Hospital Admin accounts are created by admins.
              </Typography>
            </Box>

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
                  await register({ name, email, password, dateOfBirth });
                  navigate('/login');
                } catch (err: any) {
                  setError(err?.response?.data?.message ?? 'Registration failed');
                }
              }}
            >
              <Stack spacing={2}>
                <TextField label="Full name" value={name} onChange={(e) => setName(e.target.value)} fullWidth />
                <TextField
                  label="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  autoComplete="email"
                  fullWidth
                />
                <TextField
                  label="Password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  autoComplete="new-password"
                  fullWidth
                />
                <TextField
                  label="Date of Birth"
                  type="date"
                  value={dateOfBirth}
                  onChange={(e) => setDob(e.target.value)}
                  slotProps={{ inputLabel: { shrink: true } }}
                  fullWidth
                />

                <Button type="submit" variant="contained" size="large">
                  Create patient account
                </Button>

                <Button component={RouterLink} to="/login" variant="text">
                  Back to login
                </Button>
              </Stack>
            </Box>
          </Stack>
        </GlassCard>
      </Box>
    </Container>
  );
}
