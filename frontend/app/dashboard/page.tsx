'use client'

import {useAuthStore} from "@/lib/store/authStore";

export default function DashboardPage() {
    const { user } = useAuthStore()

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold">Dashboard</h1>
            <p>Welcome, {user?.email}</p>
        </div>
    )
}
