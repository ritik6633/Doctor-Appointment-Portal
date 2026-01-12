import { Box, Button, Container, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { register } from '../api/authApi';

export function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [dateOfBirth, setDob] = useState('2000-01-01');
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 8 }}>
        <Typography variant="h4" sx={{ mb: 2 }}>
          Register (Patient)
        </Typography>

        <Box component="form"
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
          <TextField fullWidth label="Name" margin="normal" value={name} onChange={(e) => setName(e.target.value)} />
          <TextField fullWidth label="Email" margin="normal" value={email} onChange={(e) => setEmail(e.target.value)} />
          <TextField fullWidth label="Password" type="password" margin="normal" value={password} onChange={(e) => setPassword(e.target.value)} />
          <TextField fullWidth label="Date of Birth (YYYY-MM-DD)" margin="normal" value={dateOfBirth} onChange={(e) => setDob(e.target.value)} />

          {error && (
            <Typography color="error" variant="body2" sx={{ mt: 1 }}>
              {error}
            </Typography>
          )}

          <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
            Register
          </Button>

          <Button component={RouterLink} to="/login" fullWidth sx={{ mt: 1 }}>
            Back to login
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

