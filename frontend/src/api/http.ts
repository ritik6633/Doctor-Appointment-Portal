import axios from 'axios';
import { loadAuth } from '../auth/authStorage';

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
});

http.interceptors.request.use((config) => {
  const auth = loadAuth();
  if (auth?.isAuthenticated) {
    config.headers = config.headers ?? {};
    config.headers['X-USER-ID'] = String(auth.userId);
    config.headers['X-ROLE'] = auth.role;
  }
  return config;
});

