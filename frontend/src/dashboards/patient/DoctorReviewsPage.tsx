import { useEffect, useMemo, useState } from 'react';
import {
  Box,
  Button,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { listHospitals } from '../../api/hospitalApi';
import { listDoctorsByHospital } from '../../api/doctorApi';
import { createReview, listReviewsByDoctor, type ReviewResponse } from '../../api/reviewApi';

export function DoctorReviewsPage() {
  const [hospitals, setHospitals] = useState<{ id: number; name: string; city: string }[]>([]);
  const [hospitalId, setHospitalId] = useState<number | ''>('');
  const [doctors, setDoctors] = useState<{ id: number; name: string; specialization: string }[]>([]);
  const [doctorId, setDoctorId] = useState<number | ''>('');

  const [reviews, setReviews] = useState<ReviewResponse[]>([]);

  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    listHospitals().then((hs) => setHospitals(hs.map((h) => ({ id: h.id, name: h.name, city: h.city }))));
  }, []);

  useEffect(() => {
    setDoctorId('');
    setDoctors([]);
    setReviews([]);
    if (typeof hospitalId === 'number') {
      listDoctorsByHospital(hospitalId).then((ds) =>
        setDoctors(ds.map((d) => ({ id: d.id, name: d.name, specialization: d.specialization }))),
      );
    }
  }, [hospitalId]);

  useEffect(() => {
    setMsg(null);
    setReviews([]);
    if (typeof doctorId === 'number') {
      listReviewsByDoctor(doctorId).then(setReviews);
    }
  }, [doctorId]);

  const canSubmit = useMemo(() => typeof doctorId === 'number' && comment.trim().length > 3, [doctorId, comment]);

  return (
    <Container maxWidth="md">
      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Doctor Reviews
        </Typography>

        <Stack direction={{ xs: 'column', md: 'row' }} spacing={2} sx={{ mb: 2 }}>
          <FormControl fullWidth>
            <InputLabel>Hospital</InputLabel>
            <Select value={hospitalId} label="Hospital" onChange={(e) => setHospitalId(e.target.value as any)}>
              {hospitals.map((h) => (
                <MenuItem key={h.id} value={h.id}>
                  {h.name} ({h.city})
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth disabled={typeof hospitalId !== 'number'}>
            <InputLabel>Doctor</InputLabel>
            <Select value={doctorId} label="Doctor" onChange={(e) => setDoctorId(e.target.value as any)}>
              {doctors.map((d) => (
                <MenuItem key={d.id} value={d.id}>
                  {d.name} - {d.specialization}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Stack>

        {typeof doctorId === 'number' && (
          <Box sx={{ border: '1px solid #eee', borderRadius: 2, p: 2, mb: 3 }}>
            <Typography variant="h6" sx={{ mb: 1 }}>
              Add your review
            </Typography>

            <TextField
              label="Rating (1-5)"
              type="number"
              inputProps={{ min: 1, max: 5 }}
              value={rating}
              onChange={(e) => setRating(Number(e.target.value))}
              sx={{ mr: 2, width: 180 }}
            />

            <TextField
              label="Comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              fullWidth
              sx={{ mt: 2 }}
            />

            <Button
              variant="contained"
              sx={{ mt: 2 }}
              disabled={!canSubmit}
              onClick={async () => {
                setMsg(null);
                try {
                  await createReview({ doctorId, rating, comment });
                  setComment('');
                  setMsg('Review submitted');
                  const next = await listReviewsByDoctor(doctorId);
                  setReviews(next);
                } catch (e: any) {
                  setMsg(e?.response?.data?.message ?? 'Failed to submit review');
                }
              }}
            >
              Submit Review
            </Button>

            {msg && (
              <Typography sx={{ mt: 1 }} color={msg.toLowerCase().includes('fail') ? 'error' : 'primary'}>
                {msg}
              </Typography>
            )}
          </Box>
        )}

        <Typography variant="h6" sx={{ mb: 1 }}>
          Reviews
        </Typography>

        <Stack spacing={1}>
          {reviews.map((r) => (
            <Box key={r.id} sx={{ border: '1px solid #eee', borderRadius: 2, p: 2 }}>
              <Typography variant="subtitle2">
                {r.patientName} — Rating: {r.rating}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {r.comment}
              </Typography>
            </Box>
          ))}
          {typeof doctorId === 'number' && reviews.length === 0 && <Typography>No reviews yet.</Typography>}
        </Stack>
      </Box>
    </Container>
  );
}

