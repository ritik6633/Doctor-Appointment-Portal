import { useState } from 'react';
import { Alert, Button, Container, Stack, TextField, Typography } from '@mui/material';
import { useAuth } from '../../auth/AuthContext';
import { createDepartment } from '../../api/departmentApi';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';

export function ManageDepartmentsPage() {
  const { auth } = useAuth();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const hospitalId = auth?.hospitalId ?? null;

  return (
    <Container maxWidth="md">
      <PageHeader
        title="Departments"
        subtitle="Create departments for your hospital"
        breadcrumbs={[{ label: 'Hospital Admin', to: '/hospital-admin/dashboard' }, { label: 'Departments' }]}
        chip={hospitalId ? `Hospital #${hospitalId}` : undefined}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <GlassCard>
        <Stack spacing={2}>
          <Typography variant="body2" color="text.secondary">
            Hospital ID: {hospitalId ?? 'N/A'}
          </Typography>

          <TextField label="Department name" value={name} onChange={(e) => setName(e.target.value)} fullWidth />
          <TextField label="Description" value={description} onChange={(e) => setDescription(e.target.value)} fullWidth />

          <Button
            variant="contained"
            disabled={!hospitalId || !name.trim()}
            onClick={async () => {
              setMsg(null);
              try {
                await createDepartment({ hospitalId: hospitalId!, name, description });
                setName('');
                setDescription('');
                setMsg({ type: 'success', text: 'Department created' });
              } catch (e: any) {
                setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Failed to create department' });
              }
            }}
          >
            Create Department
          </Button>
        </Stack>
      </GlassCard>
    </Container>
  );
}
