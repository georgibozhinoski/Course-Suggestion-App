import {createBrowserRouter} from "react-router";
import Login from "@/pages/login.tsx";
import ProtectedRoute from "@/lib/protectedRoute.tsx";
import Homepage from "@/pages/homepage.tsx";
import {Navbar} from "@/components/navbar.tsx";


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
        path: "/",
        element: (
            <ProtectedRoute>
                <Navbar/>
            </ProtectedRoute>),
        children: [
            {
                path: "homepage",
                element: <Homepage/>
            },
            {
                path: "search",
                element: <Homepage/>
            }
        ]
    },
]);