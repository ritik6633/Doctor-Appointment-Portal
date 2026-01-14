import { AppBar, Box, Button, CssBaseline, Drawer, List, ListItemButton, ListItemText, Toolbar, Typography } from '@mui/material';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import type { Role } from '../types/auth';

const drawerWidth = 260;

function navItemsForRole(role: Role) {
  switch (role) {
    case 'PATIENT':
      return [
        { label: 'Dashboard', to: '/patient/dashboard' },
        { label: 'Book Appointment', to: '/patient/book' },
        { label: 'My Appointments', to: '/patient/appointments' },
        { label: 'Doctor Reviews', to: '/patient/reviews' },
      ];
    case 'DOCTOR':
      return [
        { label: 'Dashboard', to: '/doctor/dashboard' },
        { label: 'My Appointments', to: '/doctor/appointments' },
      ];
    case 'HOSPITAL_ADMIN':
      return [
        { label: 'Dashboard', to: '/hospital-admin/dashboard' },
        { label: 'Create Department', to: '/hospital-admin/departments' },
        { label: 'Add Doctor', to: '/hospital-admin/doctors' },
        { label: 'Manage Availability', to: '/hospital-admin/availability' },
      ];
    case 'DEVELOPER_ADMIN':
      return [
        { label: 'Dashboard', to: '/developer-admin/dashboard' },
        { label: 'Manage Hospitals', to: '/developer-admin/hospitals' },
        { label: 'Create Hospital Admin', to: '/developer-admin/hospital-admins/create' },
        { label: 'Manage Hospital Admins', to: '/developer-admin/hospital-admins' },
      ];
  }
}

export function DashboardLayout({ children }: { children: React.ReactNode }) {
  const { auth, logout } = useAuth();
  const navigate = useNavigate();

  if (!auth) return <>{children}</>;

  const items = navItemsForRole(auth.role);

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />

      <AppBar
        position="fixed"
        elevation={0}
        sx={{
          zIndex: (t) => t.zIndex.drawer + 1,
          borderBottom: '1px solid rgba(255,255,255,.10)',
          backdropFilter: 'blur(10px)',
        }}
      >
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Box>
            <Typography variant="h6" noWrap sx={{ fontWeight: 900, letterSpacing: 0.2 }}>
              Doctor Appointment Portal
            </Typography>
            <Typography variant="caption" color="text.secondary">
              {auth.role}
              {auth.hospitalId ? ` • Hospital #${auth.hospitalId}` : ''}
            </Typography>
          </Box>

          <Button
            variant="outlined"
            color="inherit"
            onClick={() => {
              logout();
              navigate('/login');
            }}
          >
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: 'border-box',
            borderRight: '1px solid rgba(255,255,255,.10)',
            backdropFilter: 'blur(10px)',
          },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: 'auto', p: 1 }}>
          <List>
            {items.map((item) => (
              <ListItemButton
                key={item.to}
                component={RouterLink}
                to={item.to}
                sx={{ borderRadius: 2, mb: 0.5 }}
              >
                <ListItemText primary={item.label} />
              </ListItemButton>
            ))}
          </List>
        </Box>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: { xs: 2, md: 3 } }}>
        <Toolbar />
        {children}
      </Box>
    </Box>
  );
}
