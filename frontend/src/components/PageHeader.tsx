import React from 'react';
import { Box, Breadcrumbs, Chip, Link, Typography } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

export type Breadcrumb = {
  label: string;
  to?: string;
};

export function PageHeader({
  title,
  subtitle,
  breadcrumbs,
  right,
  chip,
}: {
  title: string;
  subtitle?: string;
  breadcrumbs?: Breadcrumb[];
  right?: React.ReactNode;
  chip?: string;
}) {
  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: { xs: 'flex-start', md: 'center' },
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: 2,
        mb: 2,
      }}
    >
      <Box>
        {breadcrumbs && breadcrumbs.length > 0 && (
          <Breadcrumbs sx={{ mb: 0.75 }}>
            {breadcrumbs.map((b, idx) =>
              b.to ? (
                <Link
                  key={`${b.label}-${idx}`}
                  component={RouterLink}
                  to={b.to}
                  underline="hover"
                  color="inherit"
                >
                  {b.label}
                </Link>
              ) : (
                <Typography key={`${b.label}-${idx}`} color="text.secondary">
                  {b.label}
                </Typography>
              ),
            )}
          </Breadcrumbs>
        )}

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Typography variant="h5">{title}</Typography>
          {chip && <Chip size="small" label={chip} />}
        </Box>

        {subtitle && (
          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            {subtitle}
          </Typography>
        )}
      </Box>

      <Box>{right}</Box>
    </Box>
  );
}

export default PageHeader;
