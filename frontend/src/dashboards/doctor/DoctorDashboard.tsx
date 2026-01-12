import { useEffect, useState } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';

type DoctorDashboardResponse = {
  totalAppointments: number;
  todaysAppointments: number;
  completedAppointments: number;
  cancelledAppointments: number;
};

export function DoctorDashboard() {
  const [data, setData] = useState<DoctorDashboardResponse | null>(null);

  useEffect(() => {
    http.get<DoctorDashboardResponse>('/dashboard/doctor').then((r) => setData(r.data));
  }, []);

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Doctor Dashboard
      </Typography>

      <Grid container spacing={2}>
        <Grid item xs={12} md={3}>
          <StatCard title="Total" value={data?.totalAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Today's" value={data?.todaysAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Completed" value={data?.completedAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Cancelled" value={data?.cancelledAppointments ?? '-'} />
        </Grid>
      </Grid>
    </Box>
  );
}

