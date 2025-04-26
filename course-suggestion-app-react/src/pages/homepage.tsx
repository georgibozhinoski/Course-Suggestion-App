import {useAuthStore} from "@/store/authStore.ts";
import {useNavigate} from "react-router";

export default function Homepage() {
    const logout = useAuthStore((s) => s.logout);
    const navigate = useNavigate();

    const handleLogout = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            logout();
            navigate('/login');
        } catch {
            alert('Logout failed');
        }
    };

    return (
        <div className="flex min-h-svh w-full flex-col items-center justify-center p-6 md:p-10">
          <h1 className="text-center mb-6">Placeholder homepage for logged in users</h1>
          <button onClick={handleLogout}>Logout</button>
        </div>
      );
      
}