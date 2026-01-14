import { createTheme } from '@mui/material/styles';

/**
 * App-wide MUI theme.
 *
 * Goal: a clean, production-like dark UI that still matches Material defaults,
 * with better contrast and a "SaaS" feel.
 */
export const appTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#7C5CFF',
    },
    secondary: {
      main: '#34D3FF',
    },
    background: {
      default: '#070A12',
      paper: '#0E1630',
    },
  },
  shape: {
    borderRadius: 14,
  },
  typography: {
    fontFamily: [
      'ui-sans-serif',
      'system-ui',
      '-apple-system',
      'Segoe UI',
      'Roboto',
      'Helvetica',
      'Arial',
      'sans-serif',
    ].join(','),
    h5: { fontWeight: 800 },
    h6: { fontWeight: 800 },
  },
  components: {
    MuiCard: {
      styleOverrides: {
        root: {
          border: '1px solid rgba(255,255,255,.10)',
          backgroundImage:
            'linear-gradient(180deg, rgba(14,22,48,.75), rgba(11,18,41,.55))',
          backdropFilter: 'blur(10px)',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundImage:
            'linear-gradient(180deg, rgba(14,22,48,.75), rgba(11,18,41,.55))',
          borderBottom: '1px solid rgba(255,255,255,.10)',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage:
            'linear-gradient(180deg, rgba(14,22,48,.75), rgba(11,18,41,.55))',
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 700,
        },
      },
    },
  },
});

