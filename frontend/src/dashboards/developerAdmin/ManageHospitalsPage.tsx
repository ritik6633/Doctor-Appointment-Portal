import { useEffect, useState } from 'react';
import { Box, Button, Container, Stack, Typography } from '@mui/material';
import { approveHospital, listAllHospitals, type HospitalResponse } from '../../api/developerAdminApi';

export function ManageHospitalsPage() {
  const [rows, setRows] = useState<HospitalResponse[]>([]);
  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    listAllHospitals().then(setRows);
  }, []);

  return (
    <Container maxWidth="md">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Hospitals (Approve)
        </Typography>

        {msg && (
          <Typography sx={{ mb: 2 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
            {msg}
          </Typography>
        )}

        <Stack spacing={1}>
          {rows.map((h) => (
            <Box key={h.id} sx={{ border: '1px solid #eee', borderRadius: 2, p: 2 }}>
              <Typography variant="subtitle1">
                {h.name} — {h.city}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Approved: {String(h.approved)} | Active: {String(h.active)} | Email: {h.contactEmail}
              </Typography>

              {!h.approved && (
                <Button
                  sx={{ mt: 1 }}
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
              )}
            </Box>
          ))}

          {rows.length === 0 && <Typography>No hospitals found.</Typography>}
        </Stack>
      </Box>
    </Container>
  );
}

