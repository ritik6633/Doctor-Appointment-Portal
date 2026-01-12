import { useEffect, useState } from 'react';
import { Box, Button, Container, Stack, Typography } from '@mui/material';
import { cancelAppointment, listPatientAppointments, type AppointmentResponse } from '../../api/appointmentApi';
import { useAuth } from '../../auth/AuthContext';

export function MyAppointmentsPage() {
  const { auth } = useAuth();
  const [rows, setRows] = useState<AppointmentResponse[]>([]);
  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    if (!auth) return;
    listPatientAppointments(auth.userId).then(setRows);
  }, [auth]);

  return (
    <Container maxWidth="md">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          My Appointments
        </Typography>

        {msg && (
          <Typography sx={{ mb: 2 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
            {msg}
          </Typography>
        )}

        <Stack spacing={1}>
          {rows.map((r) => (
            <Box key={r.id} sx={{ border: '1px solid #eee', borderRadius: 2, p: 2 }}>
              <Typography variant="subtitle1">
                {r.appointmentDate} {r.appointmentTime} — {r.doctorName}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Status: {r.status} | Symptoms: {r.symptoms}
              </Typography>

              {r.status === 'BOOKED' && (
                <Button
                  sx={{ mt: 1 }}
                  variant="outlined"
                  color="error"
                  onClick={async () => {
                    setMsg(null);
                    try {
                      await cancelAppointment(r.id);
                      setRows((prev) => prev.map((x) => (x.id === r.id ? { ...x, status: 'CANCELLED' } : x)));
                      setMsg('Cancelled');
                    } catch (e: any) {
                      setMsg(e?.response?.data?.message ?? 'Cancel failed');
                    }
                  }}
                >
                  Cancel
                </Button>
              )}
            </Box>
          ))}

          {rows.length === 0 && <Typography>No appointments.</Typography>}
        </Stack>
      </Box>
    </Container>
  );
}

