import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,
    proxy: {
      '/auth': 'http://localhost:8080',
      '/hospitals': 'http://localhost:8080',
      '/departments': 'http://localhost:8080',
      '/doctors': 'http://localhost:8080',
      '/appointments': 'http://localhost:8080',
      '/reviews': 'http://localhost:8080',
      '/dashboard': 'http://localhost:8080',
      '/actuator': 'http://localhost:8080'
    },
  },
});
