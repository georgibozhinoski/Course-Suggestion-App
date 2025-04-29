import { create } from 'zustand';
import axios from '../api/axiosInstance.ts';

export interface UserInfo {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    studyMajor: string;
}

export interface UpdatePasswordBody {
    email: string;
    oldPassword: string;
    newPassword: string;
}

interface UserDataState {
    getUserInfo: (userId: number) => Promise<UserInfo>;
    updateUserInfo: (data: UpdateUserInfoBody) => Promise<void>;
    updatePassword: (data: UpdatePasswordBody) => Promise<void>;
}

export interface UpdateUserInfoBody {
    id: number;
    newFirstName: string;
    newLastName: string;
    newStudyMajorId: number;
}

export const useUserDataStore = create<UserDataState>(() => ({

    getUserInfo: async (userId: number):Promise<UserInfo> => {
        try {
            const res = await axios.get(`/user/${userId}`);
            return res.data;
        } catch (error) {
            console.error("Failed to fetch user info:", error);
            throw error;
        }
    },

    updateUserInfo: async (data: UpdateUserInfoBody) => {
        try {
            await axios.put('/user/update', data);
        } catch (error) {
            console.error("Failed to update user info:", error);
            throw error;
        }
    },

    updatePassword: async (data: UpdatePasswordBody) => {
        try {
            await axios.put('/user/change-password', data);
        } catch (error) {
            console.error("Failed to update password:", error);
            throw error;
        }
    },
}));
