import { useEffect, useState } from 'react';
import { Box, Grid } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';
import PageHeader from '../../components/PageHeader';

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
      <PageHeader
        title="Doctor Dashboard"
        subtitle="Today's workload and appointment stats"
        breadcrumbs={[{ label: 'Doctor' }, { label: 'Dashboard' }]}
      />

      <Grid container spacing={2}>
        <Grid item xs={12} md={3}>
          <StatCard title="Total" value={data?.totalAppointments ?? '-'} hint="All time" />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Today's" value={data?.todaysAppointments ?? '-'} hint="For today" />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Completed" value={data?.completedAppointments ?? '-'} hint="All time" />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Cancelled" value={data?.cancelledAppointments ?? '-'} hint="All time" />
        </Grid>
      </Grid>
    </Box>
  );
}
