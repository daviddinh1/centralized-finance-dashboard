import {instance} from "@/lib/api";

interface RegisterData{
    email: string;
    password: string;
    name: string;
}

interface LoginData{
    email: string;
    password: string;
}

interface AuthResponse{
    token: string;
    email:string;
    name: string;
}

export const authService = {
    register: async (data: RegisterData): Promise<AuthResponse> => {
        const response = await instance.post<AuthResponse>('/api/auth/register', data);
        return response.data;
    },

    login: async (data: LoginData): Promise<AuthResponse> => {
        const response = await instance.post<AuthResponse>('/api/auth/login', data);
        return response.data;
    },

    getCurrentUser: async (token : string | undefined) => {
        const response = await instance.get('/api/auth/me',{
            headers: {Authorization: `Bearer ${token}`}
        });
        return response.data;
    },
}