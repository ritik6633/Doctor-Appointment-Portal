import { Card, CardContent, Typography } from '@mui/material';

export function StatCard({ title, value }: { title: string; value: string | number }) {
  return (
    <Card variant="outlined">
      <CardContent>
        <Typography variant="subtitle2" color="text.secondary">
          {title}
        </Typography>
        <Typography variant="h5" sx={{ mt: 1 }}>
          {value}
        </Typography>
      </CardContent>
    </Card>
  );
}

