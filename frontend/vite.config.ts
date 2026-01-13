import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    strictPort: true,
    proxy: {
      '/auth': 'http://localhost:8081',
      '/hospitals': 'http://localhost:8081',
      '/departments': 'http://localhost:8081',
      '/doctors': 'http://localhost:8081',
      '/appointments': 'http://localhost:8081',
      '/reviews': 'http://localhost:8081',
      '/dashboard': 'http://localhost:8081',
      '/actuator': 'http://localhost:8081'
    },
  },
});
