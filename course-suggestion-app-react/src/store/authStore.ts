import { create } from 'zustand';
import axios from '../api/axiosInstance.ts';

interface RegisterBody {
    email: string;
    firstName: string;
    lastName: string;
    password: string;
}

interface AuthState {
    token: string | null;
    login: (email: string, password: string) => Promise<void>;
    register: (user: RegisterBody | FormData) => Promise<void>;
    logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
    token: localStorage.getItem('token'),

    login: async (email, password) => {
        const res = await axios.post('/auth/authenticate', { email, password });
        const token = res.data.token;
        localStorage.setItem('token', token);
        set({ token });
    },

    register: async (user) => {
        if (user instanceof FormData) {
            await axios.post('/auth/register', user, {
                headers: { 'Content-Type': 'multipart/form-data' },
            });
        } else {
            await axios.post('/auth/register', user);
        }
    },

    logout: () => {
        localStorage.removeItem('token');
        set({ token: null });
    },
}));
