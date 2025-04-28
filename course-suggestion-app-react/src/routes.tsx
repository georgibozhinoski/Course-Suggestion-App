import { createBrowserRouter } from "react-router";
import Login from "@/pages/login.tsx";
import ProtectedRoute from "@/lib/protectedRoute.tsx";
import Homepage from "@/pages/homepage.tsx";
import { Navbar } from "@/components/navbar.tsx";
import NotFoundPage from "@/pages/404.tsx";
import Register from "./pages/register";
import SearchPage from "@/pages/searchpage.tsx";
import RecommendCourses from "./pages/recommendcourses";

export const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />, // Add Register route here
  },
  {
    path: "/",
    element: (
      <ProtectedRoute>
        <Navbar />
      </ProtectedRoute>
    ),
    children: [
      {
        path: "",
        element: <Homepage />,
      },
      {
        path: "homepage",
        element: <Homepage />,
      },
      {
        path: "search",
        element: <SearchPage />,
      },
      {
        path: "recommend-courses",
        element: <RecommendCourses />,
      },
    ],
  },
  {
    path: "*",
    element: <NotFoundPage />,
  },
]);
