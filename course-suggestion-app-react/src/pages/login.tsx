import { LoginForm } from "@/components/loginForm.tsx"
import logoImage from "@/assets/EyeCoverPhoto.png"

export default function Login() {
    return (
        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10 bg-muted">
            <div className="flex w-full max-w-6xl rounded-xl overflow-hidden shadow-lg">
                {/* Left side: Login Form */}
                <div className="w-full md:w-1/2 bg-white p-6 md:p-10 flex items-center justify-center ">
                <div className="w-full max-w-md">
                    <LoginForm />
                </div>
                </div>


                {/* Right side: Image */}
                <div className="hidden md:flex w-1/2 items-center justify-center bg-gradient-to-br from-[#173d77] to-[#2563eb]">
                    <img
                        src={logoImage}
                        alt="Login Illustration"
                        className="h-full w-full object-cover"
                    />
                </div>
            </div>
        </div>
    )
}
