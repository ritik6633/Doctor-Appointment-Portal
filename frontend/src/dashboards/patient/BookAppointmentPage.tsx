import { useEffect, useMemo, useState } from 'react';
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
import { listHospitals } from '../../api/hospitalApi';
import { listDoctorsByHospital } from '../../api/doctorApi';
import { bookAppointment } from '../../api/appointmentApi';

export function BookAppointmentPage() {
  const [hospitals, setHospitals] = useState<{ id: number; name: string; city: string }[]>([]);
  const [hospitalId, setHospitalId] = useState<number | ''>('');
  const [doctors, setDoctors] = useState<{ id: number; name: string; specialization: string }[]>([]);
  const [doctorId, setDoctorId] = useState<number | ''>('');

  const [date, setDate] = useState<string>(() => new Date().toISOString().slice(0, 10));
  const [time, setTime] = useState<string>('10:00:00');
  const [symptoms, setSymptoms] = useState('');

  const [msg, setMsg] = useState<string | null>(null);

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

  const canSubmit = useMemo(() => typeof hospitalId === 'number' && typeof doctorId === 'number' && symptoms.trim(), [hospitalId, doctorId, symptoms]);

  return (
    <Container maxWidth="sm">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Book Appointment
        </Typography>

        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel>Hospital</InputLabel>
          <Select value={hospitalId} label="Hospital" onChange={(e) => setHospitalId(e.target.value as any)}>
            {hospitals.map((h) => (
              <MenuItem key={h.id} value={h.id}>
                {h.name} ({h.city})
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl fullWidth sx={{ mb: 2 }} disabled={typeof hospitalId !== 'number'}>
          <InputLabel>Doctor</InputLabel>
          <Select value={doctorId} label="Doctor" onChange={(e) => setDoctorId(e.target.value as any)}>
            {doctors.map((d) => (
              <MenuItem key={d.id} value={d.id}>
                {d.name} - {d.specialization}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <TextField fullWidth label="Date" type="date" sx={{ mb: 2 }} value={date} onChange={(e) => setDate(e.target.value)} InputLabelProps={{ shrink: true }} />
        <TextField fullWidth label="Time (HH:mm:ss)" sx={{ mb: 2 }} value={time} onChange={(e) => setTime(e.target.value)} />
        <TextField fullWidth label="Symptoms" sx={{ mb: 2 }} value={symptoms} onChange={(e) => setSymptoms(e.target.value)} />

        <Button
          variant="contained"
          fullWidth
          disabled={!canSubmit}
          onClick={async () => {
            setMsg(null);
            try {
              const res = await bookAppointment({ doctorId: doctorId as number, appointmentDate: date, appointmentTime: time, symptoms });
              setMsg(`Booked appointment #${res.id} with ${res.doctorName}`);
            } catch (e: any) {
              setMsg(e?.response?.data?.message ?? 'Booking failed');
            }
          }}
        >
          Book
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

