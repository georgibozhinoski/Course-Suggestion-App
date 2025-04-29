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
    userId: number | null;
    login: (email: string, password: string) => Promise<void>;
    register: (user: RegisterBody | FormData) => Promise<void>;
    logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
    token: localStorage.getItem('token'),
    userId: Number(localStorage.getItem('userId')),

    login: async (email, password) => {
        const res = await axios.post('/auth/authenticate', { email, password });
        const token = res.data.token;
        const userId = res.data.userId;
        localStorage.setItem('token', token);
        localStorage.setItem('userId', userId);
        set({ token });
        set({ userId: userId });
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
        localStorage.removeItem('userId');
        set({ token: null });
        set({ userId: null });
    },
}));
