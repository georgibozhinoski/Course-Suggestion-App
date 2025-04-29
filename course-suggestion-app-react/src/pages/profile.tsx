import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {FormError} from "@/components/formError";
import {useEffect, useState} from "react";
import {useAuthStore} from "@/store/authStore";
import {Button} from "@/components/ui/button";
import UpdateUserDataForm from "@/components/updateUserDataForm";
import {useUserDataStore} from "@/store/userDataStore";
import Spinner from "@/components/ui/spinner";
import {UserInfo} from "@/store/userDataStore.ts";
import UpdatePasswordForm from "@/components/updatePasswordForm.tsx";

export default function ProfilePage() {
    const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
    const [disableDataForm, setDisableDataForm] = useState(true);
    const [updatePassword, setUpdatePassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const authStore = useAuthStore();
    const userDataStore = useUserDataStore();

    useEffect(() => {
        setLoading(true);
        if (authStore.userId) {
            fetchUserData(authStore.userId);
        }
    }, [authStore.userId]);

    const fetchUserData = () =>
        authStore.userId &&
        userDataStore.getUserInfo(authStore.userId)
            .then(data => {
                console.log(data);
                setUserInfo(data);
            })
            .catch((err) => {
                console.error(err);
                setError(err.message);
            }).finally(() => setLoading(false));

    return (
        <div className="flex flex-col gap-0.5 min-h-svh w-full items-center justify-center p-6 md:p-10 bg-muted">
            <Card className="w-150">
                <CardHeader>
                    <CardTitle className="text-3xl text-center">Account Details</CardTitle>
                    <CardDescription className="text-center">Please enter your details.</CardDescription>
                </CardHeader>
                <CardContent className="flex flex-col gap-6">
                    {loading ?
                        <Spinner/>
                        : error ?
                            <FormError message={error}/>
                            : userInfo && <>
                            <UpdateUserDataForm
                                user={userInfo}
                                disabled={disableDataForm}
                                onSubmit={() => setDisableDataForm(true)}
                            />
                            {disableDataForm && (
                                <Button onClick={() => setDisableDataForm(false)}>
                                    Edit
                                </Button>
                            )}
                            {updatePassword ?
                                <UpdatePasswordForm
                                    email={userInfo.email}
                                    onSubmit={() => setUpdatePassword(false)}/>
                                : <Button
                                    variant={"outline"}
                                    onClick={() => setUpdatePassword(true)}>
                                    Change Password
                                </Button>
                            }
                        </>}
                </CardContent>
            </Card>
            <Button variant={"link"} onClick={() => authStore.logout()}>Logout</Button>
        </div>
    );
}
