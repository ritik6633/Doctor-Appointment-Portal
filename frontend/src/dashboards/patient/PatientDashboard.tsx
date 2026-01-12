import { useEffect, useState } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';

type PatientDashboardResponse = {
  totalAppointments: number;
  upcomingAppointments: number;
  pastAppointments: number;
  nextAppointment: null | {
    appointmentId: number;
    doctorId: number;
    doctorName: string;
    date: string;
    time: string;
    status: 'BOOKED' | 'CANCELLED' | 'COMPLETED';
  };
};

export function PatientDashboard() {
  const [data, setData] = useState<PatientDashboardResponse | null>(null);

  useEffect(() => {
    http.get<PatientDashboardResponse>('/dashboard/patient').then((r) => setData(r.data));
  }, []);

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Patient Dashboard
      </Typography>

      <Grid container spacing={2}>
        <Grid item xs={12} md={4}>
          <StatCard title="Total Appointments" value={data?.totalAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard title="Upcoming" value={data?.upcomingAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard title="Past" value={data?.pastAppointments ?? '-'} />
        </Grid>
      </Grid>

      <Box sx={{ mt: 3 }}>
        <Typography variant="h6">Next appointment</Typography>
        <Typography variant="body2" color="text.secondary">
          {data?.nextAppointment
            ? `${data.nextAppointment.date} ${data.nextAppointment.time} with ${data.nextAppointment.doctorName}`
            : 'No upcoming appointment'}
        </Typography>
      </Box>
    </Box>
  );
}

