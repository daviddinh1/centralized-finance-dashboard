'use client'

import {useState} from "react";
import {isAxiosError} from "axios";
import {authService} from "@/services/authService";
import {useAuthStore} from "@/lib/store/authStore";
import {useRouter} from "next/navigation";

export default function LoginPage() {
    const [formValues,setFormValues] = useState({email: "", password: ""});
    const [error,setError] = useState("");

    //declare the authStore
    const {login, setLoading, isLoading} = useAuthStore();
    const router = useRouter();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormValues({
            ...formValues,
            [name]: value,
        })
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try{
            const response = await authService.login(formValues);
            console.log("login page worked ",response);

            const token = response.token;

            const user = await authService.getCurrentUser(token);
            console.log("This is the user object", user);

            //store the users login here
            login(user, token);

            //lets push to the dashboard
            router.push("/dashboard");
        }catch(error){
            if (isAxiosError(error)) {
                // TypeScript now knows 'error' is of type AxiosError
                console.log(formValues);
                console.log('Axios error message:', error.message);
                if (error.response) {
                    // The server responded with a status code that falls out of the range of 2xx
                    console.log('Status code:', error.response.status);
                } else if (error.request) {
                    // The request was made but no response was received
                    console.log('No response received:', error.request);
                }
            } else {
                // Handle non-Axios errors (e.g., a bug in your code, network issue, etc.)
                console.log('An unexpected error occurred:', error);
            }
        }finally{
            setLoading(false);

        }
    }

    return (
        <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-white">Sign in to your
                    account</h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                {error && (
                    <div className="mb-4 bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md">
                        {error}
                    </div>
                )}
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="email" className="block text-sm/6 font-medium text-gray-100">Email
                            address</label>
                        <div className="mt-2">
                            <input id="email" type="email" name="email" value={formValues.email} onChange={handleChange} required autoComplete="email"
                                   className="block w-full rounded-md bg-white/5 px-3 py-1.5 text-base text-white outline-1 -outline-offset-1 outline-white/10 placeholder:text-gray-500 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-500 sm:text-sm/6"/>
                        </div>
                    </div>

                    <div>
                        <div className="flex items-center justify-between">
                            <label htmlFor="password"
                                   className="block text-sm/6 font-medium text-gray-100">Password</label>
                            <div className="text-sm">
                                <a href="#" className="font-semibold text-indigo-400 hover:text-indigo-300">Forgot
                                    password?</a>
                            </div>
                        </div>
                        <div className="mt-2">
                            <input id="password" type="password" name="password" value={formValues.password} onChange={handleChange} required
                                   autoComplete="current-password"
                                   className="block w-full rounded-md bg-white/5 px-3 py-1.5 text-base text-white outline-1 -outline-offset-1 outline-white/10 placeholder:text-gray-500 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-500 sm:text-sm/6"/>
                        </div>
                    </div>

                    <div>
                        <button
                            type="submit"
                            disabled={isLoading}
                            className="flex w-full justify-center rounded-md bg-indigo-500 px-3 py-1.5 text-sm/6 font-semibold text-white hover:bg-indigo-400 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {isLoading ? 'Signing in...' : 'Sign in'}
                        </button>
                    </div>
                </form>

                <p className="mt-10 text-center text-sm/6 text-gray-400">
                    Not a member?
                    <a href="#" className="font-semibold text-indigo-400 hover:text-indigo-300">Start a 14 day free
                        trial</a>
                </p>
            </div>
        </div>
    )
}
