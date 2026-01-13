import { useEffect, useMemo, useState } from 'react';
import { Box, Button, Container, Stack, Typography } from '@mui/material';
import { http } from '../../api/http';
import { useAuth } from '../../auth/AuthContext';

type AppointmentStatus = 'BOOKED' | 'CANCELLED' | 'COMPLETED';

type AppointmentResponse = {
  id: number;
  doctorId: number;
  doctorName: string;
  patientId: number;
  patientName: string;
  hospitalId: number;
  appointmentDate: string;
  appointmentTime: string;
  status: AppointmentStatus;
  symptoms: string;
};

type UpdateAppointmentStatusRequest = {
  status: AppointmentStatus;
};

export function MyDoctorAppointmentsPage() {
  const { auth } = useAuth();
  const [rows, setRows] = useState<AppointmentResponse[]>([]);
  const [msg, setMsg] = useState<string | null>(null);

  const doctorUserId = auth?.userId;

  const today = useMemo(() => new Date().toISOString().slice(0, 10), []);

  useEffect(() => {
    if (!doctorUserId) return;
    http.get<AppointmentResponse[]>(`/appointments/doctor/${doctorUserId}`).then((r) => setRows(r.data));
  }, [doctorUserId]);

  return (
    <Container maxWidth="md">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          My Appointments
        </Typography>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Today: {today}
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
                {r.appointmentDate} {r.appointmentTime} — Patient: {r.patientName}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Status: {r.status} | Symptoms: {r.symptoms}
              </Typography>

              {r.status === 'BOOKED' && (
                <Button
                  sx={{ mt: 1 }}
                  variant="contained"
                  onClick={async () => {
                    setMsg(null);
                    try {
                      const { data } = await http.put<AppointmentResponse, any, UpdateAppointmentStatusRequest>(
                        `/appointments/${r.id}/status`,
                        { status: 'COMPLETED' },
                      );
                      setRows((prev) => prev.map((x) => (x.id === r.id ? data : x)));
                      setMsg('Marked as COMPLETED');
                    } catch (e: any) {
                      setMsg(e?.response?.data?.message ?? 'Update failed');
                    }
                  }}
                >
                  Mark Completed
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

