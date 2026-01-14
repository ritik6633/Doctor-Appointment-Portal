import { useEffect, useMemo, useState } from 'react';
import {
  Alert,
  Button,
  Container,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import { http } from '../../api/http';
import { useAuth } from '../../auth/AuthContext';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';
import { StatusChip } from '../../components/StatusChip';
import { formatDateTime } from '../../utils/format';

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
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const doctorUserId = auth?.userId;
  const today = useMemo(() => new Date().toISOString().slice(0, 10), []);

  useEffect(() => {
    if (!doctorUserId) return;
    http.get<AppointmentResponse[]>(`/appointments/doctor/${doctorUserId}`).then((r) => setRows(r.data));
  }, [doctorUserId]);

  return (
    <Container maxWidth="lg">
      <PageHeader
        title="My Appointments"
        subtitle="Manage today's bookings and update statuses"
        breadcrumbs={[{ label: 'Doctor', to: '/doctor/dashboard' }, { label: 'My Appointments' }]}
        chip={`Today: ${today}`}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      {rows.length === 0 ? (
        <GlassCard>
          <Typography color="text.secondary">No appointments.</Typography>
        </GlassCard>
      ) : (
        <GlassCard sx={{ p: 0 }} contentSx={{ p: 0 }}>
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>Date & Time</TableCell>
                  <TableCell>Patient</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell>Symptoms</TableCell>
                  <TableCell align="right">Action</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((r) => (
                  <TableRow key={r.id} hover>
                    <TableCell>{formatDateTime(r.appointmentDate, r.appointmentTime)}</TableCell>
                    <TableCell>{r.patientName}</TableCell>
                    <TableCell>
                      <StatusChip status={r.status as any} />
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2" color="text.secondary">
                        {r.symptoms}
                      </Typography>
                    </TableCell>
                    <TableCell align="right">
                      <Stack direction="row" spacing={1} justifyContent="flex-end">
                        {r.status === 'BOOKED' && (
                          <Button
                            size="small"
                            variant="contained"
                            onClick={async () => {
                              setMsg(null);
                              try {
                                const { data } = await http.put<AppointmentResponse, any, UpdateAppointmentStatusRequest>(
                                  `/appointments/${r.id}/status`,
                                  { status: 'COMPLETED' },
                                );
                                setRows((prev) => prev.map((x) => (x.id === r.id ? data : x)));
                                setMsg({ type: 'success', text: 'Marked as COMPLETED' });
                              } catch (e: any) {
                                setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Update failed' });
                              }
                            }}
                          >
                            Mark Completed
                          </Button>
                        )}
                      </Stack>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </GlassCard>
      )}
    </Container>
  );
}

