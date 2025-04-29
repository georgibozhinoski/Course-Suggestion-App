import {FormError} from "@/components/formError";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Label} from "@/components/ui/label";
import {Progress} from "@/components/ui/progress";
import {useUserDataStore} from "@/store/userDataStore";
import React, {useState} from "react";
import {AxiosError} from "axios";

interface UpdatPasswordFormProps {
    email: string,
    onSubmit: () => void,
}
export default function UpdatePasswordForm({email, onSubmit}: UpdatPasswordFormProps): React.ReactElement {
    const [formError, setFormError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [progressValue, setProgressValue] = useState(0);

    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const updatePassword = useUserDataStore((state) => state.updatePassword);


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setFormError("Passwords do not match");
            return;
        }

        setLoading(true);
        setProgressValue(10);

        const interval = setInterval(() => {
            setProgressValue((prev) => {
                if (prev >= 90) {
                    clearInterval(interval);
                    return 90;
                }
                return prev + 10;
            });
        }, 500);

        try {
            await updatePassword({
                email,
                oldPassword,
                newPassword,
            });
        } catch (error: unknown) {
            if (error instanceof AxiosError) {
                setFormError(error.response?.data?.message || "Update failed");
            } else {
                setFormError("Update failed: Unknown error");
            }
            console.error(error);
        } finally {
            setLoading(false);
            clearInterval(interval);
        }

        onSubmit();
    };

    return (
        <form onSubmit={handleSubmit} encType="multipart/form-data" className="flex flex-col gap-6">
            {formError && <FormError message={formError}/>}

            <div className="grid gap-3">
                <Label htmlFor="oldPassword">Old Password</Label>
                <Input
                    id="oldPassword"
                    type="password"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                    required
                />
            </div>

            <div className="grid gap-3">
                <Label htmlFor="newPassword">New Password</Label>
                <Input
                    id="newPassword"
                    type="password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                />
            </div>

            <div className="grid gap-3">
                <Label htmlFor="confirmPassword">Confirm Password</Label>
                <Input
                    id="confirmPassword"
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                />
            </div>
            <div className="flex flex-col gap-3">
                <Button type="submit" className="w-full">
                    Update Password
                </Button>
                {loading && <Progress className="mt-1" value={progressValue}/>}
            </div>
        </form>
    );
}
