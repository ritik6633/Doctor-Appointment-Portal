import { useEffect, useState } from 'react';
import {
  Box,
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
import { cancelAppointment, listPatientAppointments, type AppointmentResponse } from '../../api/appointmentApi';
import { useAuth } from '../../auth/AuthContext';
import { GlassCard } from '../../components/GlassCard';
import PageHeader from '../../components/PageHeader';
import { StatusChip } from '../../components/StatusChip';
import { formatDateTime } from '../../utils/format';

export function MyAppointmentsPage() {
  const { auth } = useAuth();
  const [rows, setRows] = useState<AppointmentResponse[]>([]);
  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    if (!auth) return;
    listPatientAppointments(auth.userId).then(setRows);
  }, [auth]);

  return (
    <Container maxWidth="lg">
      <PageHeader
        title="My Appointments"
        subtitle="Your upcoming and past bookings"
        breadcrumbs={[{ label: 'Patient', to: '/patient/dashboard' }, { label: 'My Appointments' }]}
      />

      {msg && (
        <Typography sx={{ mb: 2 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
          {msg}
        </Typography>
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
                  <TableCell>Doctor</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell>Symptoms</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((r) => (
                  <TableRow key={r.id} hover>
                    <TableCell>{formatDateTime(r.appointmentDate, r.appointmentTime)}</TableCell>
                    <TableCell>{r.doctorName}</TableCell>
                    <TableCell>
                      <StatusChip status={r.status} />
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
                      </Stack>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </GlassCard>
      )}

      <Box sx={{ mt: 2 }} />
    </Container>
  );
}
