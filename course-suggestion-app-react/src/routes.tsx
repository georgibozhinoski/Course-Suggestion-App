import {createBrowserRouter} from "react-router";
import Login from "@/pages/login.tsx";
import ProtectedRoute from "@/lib/protectedRoute.tsx";
import Homepage from "@/pages/homepage.tsx";
import ProfilePage from "@/pages/profile.tsx";
import {Navbar} from "@/components/navbar.tsx";
import NotFoundPage from "@/pages/404.tsx";
import Register from "./pages/register";
import SearchPage from "@/pages/searchpage.tsx";
import RecommendCourses from "./pages/recommendcourses";
import ThreadPage from "./pages/threadpage";

export const router = createBrowserRouter([
    {
         path: "/login",
         element: <Login />,
    },
    {
        path: "/register",
        element: <Register />, 
    },
    {
        path: "/",
        element: (
            <ProtectedRoute>
                <Navbar/>
            </ProtectedRoute>),
        children: [
            {
                path: "",
                element: <Homepage/>
            },
            {
                path: "homepage",
                element: <Homepage/>
            },
            {
                path: "search",
                element:<SearchPage />,
            },
            {
                path: "recommend-courses",
                element: <RecommendCourses />,
            },
            {
                path: "profile",
                element: <ProfilePage/>
            },
            {
                path: "thread/:id",
                element: <ThreadPage/>
            }
        ],
    },
    {
        path: "*",
        element: <NotFoundPage />,
    },
]);