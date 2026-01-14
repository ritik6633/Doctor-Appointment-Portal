import { useEffect, useMemo, useState } from 'react';
import {
  Alert,
  Button,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Rating,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { listHospitals } from '../../api/hospitalApi';
import { listDoctorsByHospital } from '../../api/doctorApi';
import { createReview, listReviewsByDoctor, type ReviewResponse } from '../../api/reviewApi';
import PageHeader from '../../components/PageHeader';
import { GlassCard } from '../../components/GlassCard';

export function DoctorReviewsPage() {
  const [hospitals, setHospitals] = useState<{ id: number; name: string; city: string }[]>([]);
  const [hospitalId, setHospitalId] = useState<number | ''>('');
  const [doctors, setDoctors] = useState<{ id: number; name: string; specialization: string }[]>([]);
  const [doctorId, setDoctorId] = useState<number | ''>('');

  const [reviews, setReviews] = useState<ReviewResponse[]>([]);

  const [rating, setRating] = useState<number | null>(5);
  const [comment, setComment] = useState('');
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

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

  const canSubmit = useMemo(
    () => typeof doctorId === 'number' && (rating ?? 0) >= 1 && comment.trim().length > 3,
    [doctorId, rating, comment],
  );

  return (
    <Container maxWidth="lg">
      <PageHeader
        title="Doctor Reviews"
        subtitle="Read reviews and submit your feedback"
        breadcrumbs={[{ label: 'Patient', to: '/patient/dashboard' }, { label: 'Doctor Reviews' }]}
      />

      {msg && (
        <Alert sx={{ mb: 2 }} severity={msg.type} variant="outlined">
          {msg.text}
        </Alert>
      )}

      <Stack spacing={2}>
        <GlassCard>
          <Typography variant="h6" sx={{ mb: 1 }}>
            Select
          </Typography>

          <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
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
                    {d.name}  {d.specialization}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Stack>
        </GlassCard>

        {typeof doctorId === 'number' && (
          <GlassCard>
            <Typography variant="h6" sx={{ mb: 1 }}>
              Add your review
            </Typography>

            <Stack spacing={2}>
              <Stack direction={{ xs: 'column', md: 'row' }} spacing={2} alignItems={{ md: 'center' }}>
                <Typography variant="body2" color="text.secondary" sx={{ minWidth: 120 }}>
                  Rating
                </Typography>
                <Rating value={rating} onChange={(_, v) => setRating(v)} max={5} />
              </Stack>

              <TextField
                label="Comment"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                fullWidth
                multiline
                minRows={2}
              />

              <Button
                variant="contained"
                disabled={!canSubmit}
                onClick={async () => {
                  setMsg(null);
                  try {
                    await createReview({ doctorId, rating: rating ?? 5, comment });
                    setComment('');
                    setRating(5);
                    setMsg({ type: 'success', text: 'Review submitted' });
                    const next = await listReviewsByDoctor(doctorId);
                    setReviews(next);
                  } catch (e: any) {
                    setMsg({ type: 'error', text: e?.response?.data?.message ?? 'Failed to submit review' });
                  }
                }}
              >
                Submit Review
              </Button>
            </Stack>
          </GlassCard>
        )}

        <GlassCard>
          <Typography variant="h6" sx={{ mb: 1 }}>
            Reviews
          </Typography>

          <Stack spacing={1}>
            {typeof doctorId === 'number' && reviews.length === 0 ? (
              <Typography color="text.secondary">No reviews yet.</Typography>
            ) : (
              reviews.map((r) => (
                <GlassCard key={r.id} sx={{ borderRadius: 3 }}>
                  <Typography variant="subtitle2" sx={{ fontWeight: 800 }}>
                    {r.patientName}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    Rating: {r.rating}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                    {r.comment}
                  </Typography>
                </GlassCard>
              ))
            )}
          </Stack>
        </GlassCard>
      </Stack>
    </Container>
  );
}

