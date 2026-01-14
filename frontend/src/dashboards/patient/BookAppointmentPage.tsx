import { useEffect, useMemo, useState } from 'react';
import {
  Alert,
  Box,
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
import { listHospitals } from '../../api/hospitalApi';
import { listDoctorsByHospital } from '../../api/doctorApi';
import { bookAppointment } from '../../api/appointmentApi';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';

export function BookAppointmentPage() {
  const [hospitals, setHospitals] = useState<{ id: number; name: string; city: string }[]>([]);
  const [hospitalId, setHospitalId] = useState<number | ''>('');
  const [doctors, setDoctors] = useState<{ id: number; name: string; specialization: string }[]>([]);
  const [doctorId, setDoctorId] = useState<number | ''>('');

  const [date, setDate] = useState<string>(() => new Date().toISOString().slice(0, 10));
  const [time, setTime] = useState<string>('10:00:00');
  const [symptoms, setSymptoms] = useState('');

  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  useEffect(() => {
    listHospitals().then((hs) => setHospitals(hs.map((h) => ({ id: h.id, name: h.name, city: h.city }))));
  }, []);

  useEffect(() => {
    setDoctorId('');
    setDoctors([]);
    if (typeof hospitalId === 'number') {
      listDoctorsByHospital(hospitalId).then((ds) =>
        setDoctors(ds.map((d) => ({ id: d.id, name: d.name, specialization: d.specialization }))),
      );
    }
  }, [hospitalId]);

  const canSubmit = useMemo(
    () => typeof hospitalId === 'number' && typeof doctorId === 'number' && symptoms.trim().length > 3,
    [hospitalId, doctorId, symptoms],
  );

  return (
    <Container maxWidth="md">
      <PageHeader
        title="Book Appointment"
        subtitle="Choose a hospital, pick a doctor, and confirm a slot"
        breadcrumbs={[{ label: 'Patient', to: '/patient/dashboard' }, { label: 'Book Appointment' }]}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <Stack spacing={2}>
        <GlassCard>
          <Typography variant="h6" sx={{ mb: 1 }}>
            1) Select hospital & doctor
          </Typography>

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
            <FormControl fullWidth>
              <InputLabel>Hospital</InputLabel>
              <Select value={hospitalId} label="Hospital" onChange={(e) => setHospitalId(e.target.value as any)}>
                {hospitals.map((h) => (
                  <MenuItem key={h.id} value={h.id}>
                    {h.name} ({h.city})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl fullWidth disabled={typeof hospitalId !== 'number'}>
              <InputLabel>Doctor</InputLabel>
              <Select value={doctorId} label="Doctor" onChange={(e) => setDoctorId(e.target.value as any)}>
                {doctors.map((d) => (
                  <MenuItem key={d.id} value={d.id}>
                    {d.name}  {d.specialization}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Stack>
        </GlassCard>

        <GlassCard>
          <Typography variant="h6" sx={{ mb: 1 }}>
            2) Choose date/time & enter symptoms
          </Typography>

          <Stack spacing={2}>
            <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
              <TextField
                fullWidth
                label="Date"
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                InputLabelProps={{ shrink: true }}
              />
              <TextField
                fullWidth
                label="Time (HH:mm:ss)"
                value={time}
                onChange={(e) => setTime(e.target.value)}
                helperText="Example: 10:30:00"
              />
            </Stack>

            <TextField
              fullWidth
              label="Symptoms"
              value={symptoms}
              onChange={(e) => setSymptoms(e.target.value)}
              helperText="Describe what you are experiencing (min 4 characters)."
            />

            <Box>
              <Button
                variant="contained"
                disabled={!canSubmit}
                onClick={async () => {
                  setMsg(null);
                  try {
                    const res = await bookAppointment({
                      doctorId: doctorId as number,
                      appointmentDate: date,
                      appointmentTime: time,
                      symptoms,
                    });
                    setMsg({ type: 'success', text: `Booked appointment #${res.id} with ${res.doctorName}` });
                    setSymptoms('');
                  } catch (e: any) {
                    setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Booking failed' });
                  }
                }}
              >
                Confirm booking
              </Button>
            </Box>
          </Stack>
        </GlassCard>
      </Stack>
    </Container>
  );
}

