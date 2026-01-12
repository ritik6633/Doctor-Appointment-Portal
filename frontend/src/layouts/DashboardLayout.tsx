import { AppBar, Box, Button, CssBaseline, Drawer, List, ListItemButton, ListItemText, Toolbar, Typography } from '@mui/material';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import type { Role } from '../types/auth';

const drawerWidth = 260;

function navItemsForRole(role: Role) {
  switch (role) {
    case 'PATIENT':
      return [{ label: 'Dashboard', to: '/patient/dashboard' }];
    case 'DOCTOR':
      return [{ label: 'Dashboard', to: '/doctor/dashboard' }];
    case 'HOSPITAL_ADMIN':
      return [{ label: 'Dashboard', to: '/hospital-admin/dashboard' }];
    case 'DEVELOPER_ADMIN':
      return [{ label: 'Dashboard', to: '/developer-admin/dashboard' }];
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

      <AppBar position="fixed" sx={{ zIndex: (t) => t.zIndex.drawer + 1 }}>
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Typography variant="h6" noWrap>
            Doctor Appointment Portal
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
            <Typography variant="body2">{auth.role}</Typography>
            <Button
              color="inherit"
              onClick={() => {
                logout();
                navigate('/login');
              }}
            >
              Logout
            </Button>
          </Box>
        </Toolbar>
      </AppBar>

      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: 'auto' }}>
          <List>
            {items.map((item) => (
              <ListItemButton key={item.to} component={RouterLink} to={item.to}>
                <ListItemText primary={item.label} />
              </ListItemButton>
            ))}
          </List>
        </Box>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        {children}
      </Box>
    </Box>
  );
}

