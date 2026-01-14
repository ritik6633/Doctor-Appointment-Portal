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
  Typography,
} from '@mui/material';
import { useAuth } from '../../auth/AuthContext';
import { listDoctorsByHospital } from '../../api/doctorApi';
import { upsertDoctorAvailability } from '../../api/hospitalAdminApi';
import { http } from '../../api/http';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'] as const;

type Availability = {
  id: number;
  doctorId: number;
  dayOfWeek: (typeof DAYS)[number];
  startTime: string;
  endTime: string;
  slotDurationMinutes: number;
};

function isValidTime(value: string) {
  // Accept HH:mm or HH:mm:ss
  return /^([01]\d|2[0-3]):[0-5]\d(:[0-5]\d)?$/.test(value);
}

export function ManageAvailabilityPage() {
  const { auth } = useAuth();
  const hospitalId = auth?.hospitalId ?? null;

  const [doctors, setDoctors] = useState<{ id: number; name: string }[]>([]);
  const [doctorId, setDoctorId] = useState<number | ''>('');

  const [dayOfWeek, setDay] = useState<(typeof DAYS)[number]>('MONDAY');
  const [startTime, setStart] = useState('10:00:00');
  const [endTime, setEnd] = useState('13:00:00');
  const [slotDurationMinutes, setSlot] = useState(15);
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const [existing, setExisting] = useState<Availability | null>(null);

  const timeError = useMemo(() => {
    if (!isValidTime(startTime) || !isValidTime(endTime)) return 'Time must be HH:mm or HH:mm:ss';
    // Compare as strings in HH:mm:ss format. Normalize to HH:mm:ss.
    const s = startTime.length === 5 ? `${startTime}:00` : startTime;
    const e = endTime.length === 5 ? `${endTime}:00` : endTime;
    if (e <= s) return 'End time must be after start time';
    return null;
  }, [startTime, endTime]);

  const slotError = useMemo(() => {
    if (!Number.isFinite(slotDurationMinutes) || slotDurationMinutes <= 0) return 'Slot duration must be > 0';
    if (slotDurationMinutes > 180) return 'Slot duration seems too large';
    return null;
  }, [slotDurationMinutes]);

  const canSave = useMemo(() => typeof doctorId === 'number' && !timeError && !slotError, [doctorId, timeError, slotError]);

  useEffect(() => {
    setDoctors([]);
    setDoctorId('');
    if (hospitalId) {
      listDoctorsByHospital(hospitalId).then((ds) => setDoctors(ds.map((d) => ({ id: d.id, name: d.name }))));
    }
  }, [hospitalId]);

  useEffect(() => {
    setExisting(null);
    if (typeof doctorId !== 'number') return;

    http
      .get<Availability[]>(`/doctors/${doctorId}/availability`)
      .then((r) => {
        const match = r.data.find((x) => x.dayOfWeek === dayOfWeek) ?? null;
        setExisting(match);
        if (match) {
          setStart(match.startTime);
          setEnd(match.endTime);
          setSlot(match.slotDurationMinutes);
        }
      })
      .catch(() => {
        // ignore
      });
  }, [doctorId, dayOfWeek]);

  return (
    <Container maxWidth="md">
      <PageHeader
        title="Doctor Availability"
        subtitle="Set weekly schedule used for booking"
        breadcrumbs={[{ label: 'Hospital Admin', to: '/hospital-admin/dashboard' }, { label: 'Availability' }]}
        chip={hospitalId ? `Hospital #${hospitalId}` : undefined}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <Stack spacing={2}>
        <GlassCard>
          <Stack spacing={2}>
            <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
              <FormControl fullWidth disabled={!hospitalId}>
                <InputLabel>Doctor</InputLabel>
                <Select value={doctorId} label="Doctor" onChange={(e) => setDoctorId(e.target.value as any)}>
                  {doctors.map((d) => (
                    <MenuItem key={d.id} value={d.id}>
                      {d.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <FormControl fullWidth>
                <InputLabel>Day of week</InputLabel>
                <Select value={dayOfWeek} label="Day of week" onChange={(e) => setDay(e.target.value as any)}>
                  {DAYS.map((d) => (
                    <MenuItem key={d} value={d}>
                      {d}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Stack>

            {typeof doctorId === 'number' && (
              <Typography variant="body2" color="text.secondary">
                {existing
                  ? `Existing for ${dayOfWeek}: ${existing.startTime} - ${existing.endTime} (${existing.slotDurationMinutes} min)`
                  : `No availability set yet for ${dayOfWeek}.`}
              </Typography>
            )}

            <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
              <TextField
                fullWidth
                label="Start time"
                value={startTime}
                onChange={(e) => setStart(e.target.value)}
                error={!!timeError}
                helperText={timeError ?? 'Example: 10:00:00'}
              />
              <TextField
                fullWidth
                label="End time"
                value={endTime}
                onChange={(e) => setEnd(e.target.value)}
                error={!!timeError}
              />
            </Stack>

            <TextField
              fullWidth
              label="Slot duration (minutes)"
              type="number"
              value={slotDurationMinutes}
              onChange={(e) => setSlot(Number(e.target.value))}
              error={!!slotError}
              helperText={slotError ?? ''}
            />

            <Button
              variant="contained"
              disabled={!canSave}
              onClick={async () => {
                setMsg(null);
                try {
                  // Normalize time to HH:mm:ss
                  const start = startTime.length === 5 ? `${startTime}:00` : startTime;
                  const end = endTime.length === 5 ? `${endTime}:00` : endTime;

                  await upsertDoctorAvailability(doctorId as number, {
                    dayOfWeek,
                    startTime: start,
                    endTime: end,
                    slotDurationMinutes,
                  });
                  setMsg({ type: 'success', text: 'Availability saved' });
                } catch (e: any) {
                  setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Failed' });
                }
              }}
            >
              Save
            </Button>
          </Stack>
        </GlassCard>
      </Stack>
    </Container>
  );
}

