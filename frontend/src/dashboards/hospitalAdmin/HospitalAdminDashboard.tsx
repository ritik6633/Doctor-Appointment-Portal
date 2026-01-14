import { useEffect, useState } from 'react';
import { Box, Grid } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';
import PageHeader from '../../components/PageHeader';

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
      <PageHeader
        title="Hospital Admin Dashboard"
        subtitle="Manage hospital operations and track bookings"
        breadcrumbs={[{ label: 'Hospital Admin' }, { label: 'Dashboard' }]}
        chip={data?.hospitalId ? `Hospital #${data.hospitalId}` : undefined}
      />

      <Grid container spacing={2}>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Doctors" value={data?.totalDoctors ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Appointments" value={data?.totalAppointments ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Booked" value={data?.bookedAppointments ?? '-'} hint="Active bookings" />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Hospital Id" value={data?.hospitalId ?? '-'} hint="Tenant" />
        </Grid>
      </Grid>
    </Box>
  );
}
