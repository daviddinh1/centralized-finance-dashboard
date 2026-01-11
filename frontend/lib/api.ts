import axios from "axios";
import {useAuthStore} from "@/lib/store/authStore";

export const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL|| 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    }
});

//req interceptor add jwt token to reqs
instance.interceptors.request.use(
    (config) => {
        const token = useAuthStore.getState().token;
        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
)

//response interceptor handles 401 errors
instance.interceptors.response.use(
    (response) => response,
    (error) => {
        if(error.response?.status === 401){
            useAuthStore.getState().logout();
        }
        return Promise.reject(error);
    }
);

