import { useEffect, useState } from 'react';
import { Box, Grid } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';
import PageHeader from '../../components/PageHeader';

type DeveloperAdminDashboardResponse = {
  totalHospitals: number;
  approvedHospitals: number;
  pendingHospitals: number;
  totalUsers: number;
  totalPatients: number;
  totalDoctors: number;
  totalHospitalAdmins: number;
  recentHospitals: Array<{ hospitalId: number; name: string; city: string; approved: boolean; active: boolean }>;
};

export function DeveloperAdminDashboard() {
  const [data, setData] = useState<DeveloperAdminDashboardResponse | null>(null);

  useEffect(() => {
    http.get<DeveloperAdminDashboardResponse>('/dashboard/developer-admin').then((r) => setData(r.data));
  }, []);

  return (
    <Box>
      <PageHeader
        title="Developer Admin Dashboard"
        subtitle="Platform overview and tenant approvals"
        breadcrumbs={[{ label: 'Developer Admin' }, { label: 'Dashboard' }]}
      />

      <Grid container spacing={2}>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Hospitals" value={data?.totalHospitals ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Approved" value={data?.approvedHospitals ?? '-'} hint="Live tenants" />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Pending" value={data?.pendingHospitals ?? '-'} hint="Need approval" />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Users" value={data?.totalUsers ?? '-'} hint="Across platform" />
        </Grid>
      </Grid>
    </Box>
  );
}
