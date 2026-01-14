import { Chip } from '@mui/material';
import type { AppointmentStatus } from '../api/appointmentApi';

export function StatusChip({ status }: { status: AppointmentStatus }) {
  const color =
    status === 'BOOKED' ? 'info' : status === 'COMPLETED' ? 'success' : status === 'CANCELLED' ? 'default' : 'default';
  return <Chip size="small" color={color as any} label={status} />;
}

