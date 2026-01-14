import { Card, CardContent, type SxProps, type Theme } from '@mui/material';

export function GlassCard({
  children,
  sx,
  contentSx,
}: {
  children: React.ReactNode;
  sx?: SxProps<Theme>;
  contentSx?: SxProps<Theme>;
}) {
  return (
    <Card
      variant="outlined"
      sx={{
        borderColor: 'rgba(255,255,255,.10)',
        borderRadius: 4,
        ...sx,
      }}
    >
      <CardContent sx={{ p: { xs: 2, md: 2.5 }, '&:last-child': { pb: { xs: 2, md: 2.5 } }, ...contentSx }}>
        {children}
      </CardContent>
    </Card>
  );
}

