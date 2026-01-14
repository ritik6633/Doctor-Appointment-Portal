import { useEffect, useState } from 'react';
import {
  Alert,
  Button,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { useAuth } from '../../auth/AuthContext';
import { listDepartmentsByHospital } from '../../api/departmentApi';
import { createDoctor } from '../../api/hospitalAdminApi';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';

export function AddDoctorPage() {
  const { auth } = useAuth();
  const hospitalId = auth?.hospitalId ?? null;

  const [departments, setDepartments] = useState<{ id: number; name: string }[]>([]);
  const [departmentId, setDepartmentId] = useState<number | ''>('');

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('Doc@123');
  const [phone, setPhone] = useState('');
  const [gender, setGender] = useState('');
  const [dateOfBirth, setDateOfBirth] = useState('1988-01-01');

  const [specialization, setSpecialization] = useState('');
  const [qualification, setQualification] = useState('');
  const [experienceYears, setExperienceYears] = useState<number | ''>('');
  const [consultationFee, setConsultationFee] = useState(500);

  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  useEffect(() => {
    setDepartments([]);
    setDepartmentId('');
    if (hospitalId) {
      listDepartmentsByHospital(hospitalId).then((d) => setDepartments(d.map((x) => ({ id: x.id, name: x.name }))));
    }
  }, [hospitalId]);

  return (
    <Container maxWidth="md">
      <PageHeader
        title="Register Doctor"
        subtitle="Create a doctor login and profile for your hospital"
        breadcrumbs={[{ label: 'Hospital Admin', to: '/hospital-admin/dashboard' }, { label: 'Register Doctor' }]}
        chip={hospitalId ? `Hospital #${hospitalId}` : undefined}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <GlassCard>
        <Stack spacing={2}>
          <FormControl fullWidth disabled={!hospitalId}>
            <InputLabel>Department</InputLabel>
            <Select value={departmentId} label="Department" onChange={(e) => setDepartmentId(e.target.value as any)}>
              {departments.map((d) => (
                <MenuItem key={d.id} value={d.id}>
                  {d.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
            <TextField label="Doctor Name" value={name} onChange={(e) => setName(e.target.value)} fullWidth />
            <TextField label="Doctor Email" value={email} onChange={(e) => setEmail(e.target.value)} fullWidth />
          </Stack>

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
            <TextField label="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} fullWidth />
            <TextField label="Gender" value={gender} onChange={(e) => setGender(e.target.value)} fullWidth />
            <TextField
              label="Date of Birth"
              type="date"
              value={dateOfBirth}
              onChange={(e) => setDateOfBirth(e.target.value)}
              slotProps={{ inputLabel: { shrink: true } }}
              fullWidth
            />
          </Stack>

          <TextField label="Temporary Password" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth />

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
            <TextField label="Specialization" value={specialization} onChange={(e) => setSpecialization(e.target.value)} fullWidth />
            <TextField label="Qualification" value={qualification} onChange={(e) => setQualification(e.target.value)} fullWidth />
          </Stack>

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
            <TextField
              label="Experience (years)"
              type="number"
              value={experienceYears}
              onChange={(e) => setExperienceYears(e.target.value === '' ? '' : Number(e.target.value))}
              fullWidth
            />
            <TextField
              label="Consultation Fee"
              type="number"
              value={consultationFee}
              onChange={(e) => setConsultationFee(Number(e.target.value))}
              fullWidth
            />
          </Stack>

          <Button
            variant="contained"
            disabled={
              !hospitalId ||
              typeof departmentId !== 'number' ||
              !name.trim() ||
              !email.trim() ||
              !password.trim() ||
              !specialization.trim() ||
              !String(consultationFee).trim()
            }
            onClick={async () => {
              setMsg(null);
              try {
                await createDoctor({
                  hospitalId: hospitalId!,
                  departmentId: departmentId as number,
                  name,
                  email,
                  password,
                  phone: phone || undefined,
                  gender: gender || undefined,
                  dateOfBirth: dateOfBirth || undefined,
                  specialization,
                  qualification: qualification || undefined,
                  experienceYears: typeof experienceYears === 'number' ? experienceYears : undefined,
                  consultationFee,
                });

                setMsg({ type: 'success', text: 'Doctor registered successfully' });

                setName('');
                setEmail('');
                setPhone('');
                setGender('');
                setQualification('');
                setExperienceYears('');
                setSpecialization('');
              } catch (e: any) {
                setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Failed to register doctor' });
              }
            }}
          >
            Register Doctor
          </Button>

          <Typography variant="caption" color="text.secondary">
            Note: The doctor can now login from the Doctor portal using their email + password.
          </Typography>
        </Stack>
      </GlassCard>
    </Container>
  );
}

