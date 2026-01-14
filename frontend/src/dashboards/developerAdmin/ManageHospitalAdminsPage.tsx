import { useEffect, useMemo, useState } from 'react';
import {
  Alert,
  Button,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import {
  listHospitalAdmins,
  resetHospitalAdminPassword,
  setHospitalAdminActive,
  type HospitalAdminUserResponse,
} from '../../api/developerAdminApi';
import { GlassCard } from '../../components/GlassCard';
import PageHeader from '../../components/PageHeader';

export function ManageHospitalAdminsPage() {
  const [rows, setRows] = useState<HospitalAdminUserResponse[]>([]);
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const [resetUser, setResetUser] = useState<HospitalAdminUserResponse | null>(null);
  const [newPassword, setNewPassword] = useState('Admin@123');

  const approvedCount = useMemo(() => rows.filter((r) => r.active).length, [rows]);

  async function reload() {
    const data = await listHospitalAdmins();
    setRows(data);
  }

  useEffect(() => {
    reload();
  }, []);

  return (
    <Container maxWidth="lg">
      <PageHeader
        title="Manage Hospital Admins"
        subtitle="Activate/deactivate hospital admins and reset passwords"
        breadcrumbs={[{ label: 'Developer Admin', to: '/developer-admin/dashboard' }, { label: 'Hospital Admins' }]}
      />

      <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 2 }}>
        Active admins: {approvedCount} / {rows.length}
      </Typography>

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <GlassCard sx={{ p: 0 }} contentSx={{ p: 0 }}>
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Hospital</TableCell>
                <TableCell>Active</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((u) => (
                <TableRow key={u.userId} hover>
                  <TableCell>{u.name}</TableCell>
                  <TableCell>{u.email}</TableCell>
                  <TableCell>{u.hospitalName ? `${u.hospitalName} (#${u.hospitalId})` : '-'}</TableCell>
                  <TableCell>{String(u.active)}</TableCell>
                  <TableCell align="right">
                    <Stack direction="row" spacing={1} justifyContent="flex-end">
                      <Button
                        size="small"
                        variant={u.active ? 'outlined' : 'contained'}
                        color={u.active ? 'warning' : 'success'}
                        onClick={async () => {
                          setMsg(null);
                          try {
                            const updated = await setHospitalAdminActive(u.userId, !u.active);
                            setRows((prev) => prev.map((x) => (x.userId === u.userId ? updated : x)));
                            setMsg({ type: 'success', text: `Updated active=${String(updated.active)} for ${u.email}` });
                          } catch (e: any) {
                            setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Update failed' });
                          }
                        }}
                      >
                        {u.active ? 'Deactivate' : 'Activate'}
                      </Button>

                      <Button
                        size="small"
                        variant="outlined"
                        onClick={() => {
                          setResetUser(u);
                          setNewPassword('Admin@123');
                        }}
                      >
                        Reset Password
                      </Button>
                    </Stack>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </GlassCard>

      <Dialog open={!!resetUser} onClose={() => setResetUser(null)} maxWidth="xs" fullWidth>
        <DialogTitle>Reset Password</DialogTitle>
        <DialogContent>
          <Stack spacing={1} sx={{ mt: 1 }}>
            <Typography variant="body2" color="text.secondary">
              User: {resetUser?.email}
            </Typography>
            <TextField
              label="New Password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              type="password"
              fullWidth
            />
            <Typography variant="caption" color="text.secondary">
              This sets a new BCrypt password. Share it securely with the hospital admin.
            </Typography>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setResetUser(null)}>Cancel</Button>
          <Button
            variant="contained"
            disabled={!resetUser || !newPassword.trim()}
            onClick={async () => {
              setMsg(null);
              try {
                await resetHospitalAdminPassword(resetUser!.userId, newPassword);
                setMsg({ type: 'success', text: `Password updated for ${resetUser!.email}` });
                setResetUser(null);
              } catch (e: any) {
                setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Reset failed' });
              }
            }}
          >
            Update Password
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

