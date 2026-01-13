import { useState } from 'react';
import { Box, Button, Container, TextField, Typography } from '@mui/material';
import { useAuth } from '../../auth/AuthContext';
import { createDepartment } from '../../api/departmentApi';

export function ManageDepartmentsPage() {
  const { auth } = useAuth();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [msg, setMsg] = useState<string | null>(null);

  const hospitalId = auth?.hospitalId ?? null;

  return (
    <Container maxWidth="sm">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Create Department
        </Typography>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Hospital ID: {hospitalId ?? 'N/A'}
        </Typography>

        <TextField fullWidth sx={{ mb: 2 }} label="Department name" value={name} onChange={(e) => setName(e.target.value)} />
        <TextField fullWidth sx={{ mb: 2 }} label="Description" value={description} onChange={(e) => setDescription(e.target.value)} />

        <Button
          variant="contained"
          fullWidth
          disabled={!hospitalId || !name.trim()}
          onClick={async () => {
            setMsg(null);
            try {
              await createDepartment({ hospitalId: hospitalId!, name, description });
              setName('');
              setDescription('');
              setMsg('Department created');
            } catch (e: any) {
              setMsg(e?.response?.data?.message ?? 'Failed');
            }
          }}
        >
          Create
        </Button>

        {msg && (
          <Typography sx={{ mt: 2 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
            {msg}
          </Typography>
        )}
      </Box>
    </Container>
  );
}

