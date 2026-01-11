import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';

interface User {
    email: string;
    name: string;
}

interface AuthState {
    // State
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    isLoading: boolean;

    // Actions
    setUser: (user: User) => void;
    setToken: (token: string) => void;
    login: (user: User, token: string) => void;
    logout: () => void;
    setLoading: (loading: boolean) => void;
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set) => ({
            // Initial state
            user: null,
            token: null,
            isAuthenticated: false,
            isLoading: false,

            // Actions
            setUser: (user) =>
                set({ user, isAuthenticated: true }),

            setToken: (token) =>
                set({ token }),

            login: (user, token) =>
                set({
                    user,
                    token,
                    isAuthenticated: true
                }),

            logout: () =>
                set({
                    user: null,
                    token: null,
                    isAuthenticated: false
                }),

            setLoading: (loading) =>
                set({ isLoading: loading }),
        }),
        {
            name: 'auth-storage', // Key in localStorage
            storage: createJSONStorage(() => localStorage),
            // Only persist specific fields
            partialize: (state) => ({
                user: state.user,
                token: state.token,
                isAuthenticated: state.isAuthenticated
            }),
        }
    )
);