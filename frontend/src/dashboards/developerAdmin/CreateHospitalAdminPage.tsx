import { useEffect, useMemo, useState } from 'react';
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
} from '@mui/material';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';
import { createHospitalAdmin, listAllHospitals, type HospitalResponse } from '../../api/developerAdminApi';

export function CreateHospitalAdminPage() {
  const [hospitals, setHospitals] = useState<HospitalResponse[]>([]);
  const [hospitalId, setHospitalId] = useState<number | ''>('');

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('Admin@123');
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const selectedHospital = useMemo(
    () => hospitals.find((h) => h.id === hospitalId) ?? null,
    [hospitals, hospitalId],
  );

  useEffect(() => {
    listAllHospitals().then((rows) => setHospitals(rows));
  }, []);

  return (
    <Container maxWidth="md">
      <PageHeader
        title="Create Hospital Admin"
        subtitle="Create a hospital admin user for a selected tenant"
        breadcrumbs={[{ label: 'Developer Admin', to: '/developer-admin/dashboard' }, { label: 'Create Hospital Admin' }]}
        chip={selectedHospital ? `${selectedHospital.name} • ${selectedHospital.city}` : undefined}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <GlassCard>
        <Stack spacing={2}>
          <FormControl fullWidth>
            <InputLabel>Hospital</InputLabel>
            <Select value={hospitalId} label="Hospital" onChange={(e) => setHospitalId(e.target.value as any)}>
              {hospitals.map((h) => (
                <MenuItem key={h.id} value={h.id}>
                  {h.name} — {h.city} {h.approved ? '' : ' (Pending)'}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
            <TextField label="Full name" value={name} onChange={(e) => setName(e.target.value)} fullWidth />
            <TextField label="Email" value={email} onChange={(e) => setEmail(e.target.value)} fullWidth />
          </Stack>

          <TextField label="Temporary password" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth />

          <Button
            variant="contained"
            disabled={typeof hospitalId !== 'number' || !name.trim() || !email.trim() || !password.trim()}
            onClick={async () => {
              setMsg(null);
              try {
                const res = await createHospitalAdmin({ hospitalId: hospitalId as number, name, email, password });
                setMsg({ type: 'success', text: `Hospital admin created (userId=${res.userId})` });
                setName('');
                setEmail('');
              } catch (e: any) {
                setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Failed to create hospital admin' });
              }
            }}
          >
            Create Hospital Admin
          </Button>
        </Stack>
      </GlassCard>
    </Container>
  );
}

