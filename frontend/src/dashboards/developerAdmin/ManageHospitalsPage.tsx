import { useEffect, useState } from 'react';
import {
  Button,
  Container,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import { approveHospital, listAllHospitals, type HospitalResponse } from '../../api/developerAdminApi';
import { GlassCard } from '../../components/GlassCard';
import PageHeader from '../../components/PageHeader';

export function ManageHospitalsPage() {
  const [rows, setRows] = useState<HospitalResponse[]>([]);
  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    listAllHospitals().then(setRows);
  }, []);

  return (
    <Container maxWidth="lg">
      <PageHeader
        title="Manage Hospitals"
        subtitle="Approve hospitals and monitor platform tenants"
        breadcrumbs={[{ label: 'Developer Admin', to: '/developer-admin/dashboard' }, { label: 'Hospitals' }]}
      />

      {msg && (
        <Typography sx={{ mb: 2 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
          {msg}
        </Typography>
      )}

      <GlassCard sx={{ p: 0 }} contentSx={{ p: 0 }}>
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Hospital</TableCell>
                <TableCell>City</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Approved</TableCell>
                <TableCell>Active</TableCell>
                <TableCell align="right">Action</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((h) => (
                <TableRow key={h.id} hover>
                  <TableCell>{h.name}</TableCell>
                  <TableCell>{h.city}</TableCell>
                  <TableCell>{h.contactEmail}</TableCell>
                  <TableCell>{String(h.approved)}</TableCell>
                  <TableCell>{String(h.active)}</TableCell>
                  <TableCell align="right">
                    {!h.approved ? (
                      <Button
                        size="small"
                        variant="contained"
                        onClick={async () => {
                          setMsg(null);
                          try {
                            await approveHospital(h.id);
                            setRows((prev) => prev.map((x) => (x.id === h.id ? { ...x, approved: true } : x)));
                            setMsg('Hospital approved');
                          } catch (e: any) {
                            setMsg(e?.response?.data?.message ?? 'Approve failed');
                          }
                        }}
                      >
                        Approve
                      </Button>
                    ) : (
                      <Typography variant="body2" color="text.secondary">
                        Approved
                      </Typography>
                    )}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </GlassCard>
    </Container>
  );
}
