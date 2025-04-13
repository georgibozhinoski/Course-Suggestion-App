import {createBrowserRouter} from "react-router";
import Login from "@/pages/login.tsx";
import ProtectedRoute from "@/lib/protectedRoute.tsx";
import Homepage from "@/pages/homepage.tsx";


export const router = createBrowserRouter([
     {
         path: "/",
         element: Login(),
     },
    {
         path: "/login",
         element: <Login />,
     },
    {
        path: "/homepage",
        element: (
            <ProtectedRoute>
                <Homepage/>
            </ProtectedRoute>),
    },
]);