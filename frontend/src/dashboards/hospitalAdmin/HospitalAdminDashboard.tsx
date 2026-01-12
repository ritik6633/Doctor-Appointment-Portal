import { useEffect, useState } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';

type HospitalAdminDashboardResponse = {
  hospitalId: number;
  totalDoctors: number;
  totalAppointments: number;
  bookedAppointments: number;
};

export function HospitalAdminDashboard() {
  const [data, setData] = useState<HospitalAdminDashboardResponse | null>(null);

  useEffect(() => {
    http.get<HospitalAdminDashboardResponse>('/dashboard/hospital-admin').then((r) => setData(r.data));
  }, []);

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Hospital Admin Dashboard
      </Typography>

      <Grid container spacing={2}>
        <Grid item xs={12} md={3}>
          <StatCard title="Hospital ID" value={data?.hospitalId ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Doctors" value={data?.totalDoctors ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Appointments" value={data?.totalAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Booked" value={data?.bookedAppointments ?? '-'} />
        </Grid>
      </Grid>
    </Box>
  );
}

