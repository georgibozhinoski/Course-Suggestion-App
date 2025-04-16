import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useState } from "react"
import { useAuthStore } from "@/store/authStore.ts"
import { useNavigate } from "react-router"
import { FormError } from "./formError"

export function LoginForm({
    className,
    ...props
}: React.ComponentProps<"div">) {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [formError, setFormError] = useState('')
    const login = useAuthStore((s) => s.login)
    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setFormError('') // clear previous error
        try {
            await login(email, password)
            navigate('/homepage')
        } catch (error) {
            setFormError('Login failed. Please check your credentials.')
            console.error(error)
        }
    }

    return (
        <div className={cn("flex flex-col gap-6 p-1/2 ", className)} {...props}>
            <Card>
                <CardHeader>
                <CardTitle className="text-center text-3xl font-semibold" >
                LOGIN
                </CardTitle>
                    <CardDescription className="text-center">
                    Welcome back! Please enter your details.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit}>
                        <div className="flex flex-col gap-6">
                            {formError && <FormError message={formError} />}
                            <div className="grid gap-3">
                                <Label htmlFor="email">Email</Label>
                                <Input
                                    id="email"
                                    type="email"
                                    placeholder="Enter your email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="grid gap-3">
                                <div className="flex items-center">
                                    <Label htmlFor="password">Password</Label>
                                    <a
                                        href="#"
                                        className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                                    >
                                        Forgot your password?
                                    </a>
                                </div>
                                <Input
                                    id="password"
                                    type="password"
                                    value={password}
                                    placeholder="**********"
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="flex flex-col gap-3">
                                <Button type="submit" className="w-full">
                                    Login
                                </Button>
                            </div>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            Don&apos;t have an account?{" "}
                            <a href="/register" className="text-blue-500 hover:underline">
                                Sign up for free
                            </a>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}
