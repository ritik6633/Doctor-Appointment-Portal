import { useEffect, useState } from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { http } from '../../api/http';
import { StatCard } from '../../components/StatCard';

type DeveloperAdminDashboardResponse = {
  totalHospitals: number;
  approvedHospitals: number;
  pendingHospitals: number;
  totalUsers: number;
};

export function DeveloperAdminDashboard() {
  const [data, setData] = useState<DeveloperAdminDashboardResponse | null>(null);

  useEffect(() => {
    http.get<DeveloperAdminDashboardResponse>('/dashboard/developer-admin').then((r) => setData(r.data));
  }, []);

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 2 }}>
        Developer Admin Dashboard
      </Typography>

      <Grid container spacing={2}>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Hospitals" value={data?.totalHospitals ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Approved" value={data?.approvedHospitals ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Pending" value={data?.pendingHospitals ?? '-'} />
        </Grid>
        <Grid item xs={12} md={3}>
          <StatCard title="Total Users" value={data?.totalUsers ?? '-'} />
        </Grid>
      </Grid>
    </Box>
  );
}

