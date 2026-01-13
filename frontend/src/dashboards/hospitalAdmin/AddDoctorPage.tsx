import { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material';
import { useAuth } from '../../auth/AuthContext';
import { listDepartmentsByHospital } from '../../api/departmentApi';
import { createDoctor } from '../../api/hospitalAdminApi';

export function AddDoctorPage() {
  const { auth } = useAuth();
  const hospitalId = auth?.hospitalId ?? null;

  const [departments, setDepartments] = useState<{ id: number; name: string }[]>([]);
  const [departmentId, setDepartmentId] = useState<number | ''>('');

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('Doc@123');
  const [specialization, setSpecialization] = useState('');
  const [consultationFee, setConsultationFee] = useState(500);
  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    setDepartments([]);
    setDepartmentId('');
    if (hospitalId) {
      listDepartmentsByHospital(hospitalId).then((d) => setDepartments(d.map((x) => ({ id: x.id, name: x.name }))));
    }
  }, [hospitalId]);

  return (
    <Container maxWidth="sm">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Add Doctor
        </Typography>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Hospital ID: {hospitalId ?? 'N/A'}
        </Typography>

        <FormControl fullWidth sx={{ mb: 2 }} disabled={!hospitalId}>
          <InputLabel>Department</InputLabel>
          <Select value={departmentId} label="Department" onChange={(e) => setDepartmentId(e.target.value as any)}>
            {departments.map((d) => (
              <MenuItem key={d.id} value={d.id}>
                {d.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <TextField fullWidth sx={{ mb: 2 }} label="Name" value={name} onChange={(e) => setName(e.target.value)} />
        <TextField fullWidth sx={{ mb: 2 }} label="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <TextField fullWidth sx={{ mb: 2 }} label="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <TextField fullWidth sx={{ mb: 2 }} label="Specialization" value={specialization} onChange={(e) => setSpecialization(e.target.value)} />
        <TextField
          fullWidth
          sx={{ mb: 2 }}
          label="Consultation Fee"
          type="number"
          value={consultationFee}
          onChange={(e) => setConsultationFee(Number(e.target.value))}
        />

        <Button
          variant="contained"
          fullWidth
          disabled={!hospitalId || typeof departmentId !== 'number' || !name.trim() || !email.trim() || !specialization.trim()}
          onClick={async () => {
            setMsg(null);
            try {
              await createDoctor({
                hospitalId: hospitalId!,
                departmentId: departmentId as number,
                name,
                email,
                password,
                specialization,
                consultationFee,
              });
              setMsg('Doctor created');
              setName('');
              setEmail('');
              setSpecialization('');
            } catch (e: any) {
              setMsg(e?.response?.data?.message ?? 'Failed');
            }
          }}
        >
          Create Doctor
        </Button>

        {msg && (
          <Typography sx={{ mt: 2 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
            {msg}
          </Typography>
        )}
      </Box>
    </Container>
  );
}

