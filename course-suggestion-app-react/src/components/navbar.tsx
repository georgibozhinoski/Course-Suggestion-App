import { useEffect, useState } from "react";
import { NavLink, Outlet } from "react-router-dom";
import logo from "@/assets/logo.webp";
import { useUserDataStore } from "@/store/userDataStore";
import ThemeToggle from "./themeToggle";

const centerMenuLinks = [
  { text: "Home", url: "/homepage" },
  { text: "Search", url: "/search" },
];

export function Navbar() {
  const userInfo = useUserDataStore((state) => state.userInfo);
  const getUserInfo = useUserDataStore((state) => state.getUserInfo);

  const [avatarUrl, setAvatarUrl] = useState<string | null>(null);

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    const userId = storedUserId ? Number(storedUserId) : null;

    if (!userInfo && userId) {
      getUserInfo(userId)
        .then((data) => {
          console.log("Fetched userInfo:", data);
          setAvatarUrl(data.avatarUrl ?? null); 
        })
        .catch((err) => console.error("Failed to preload user info:", err));
    } else if (userInfo) {
      setAvatarUrl(userInfo.avatarUrl ?? null); 
      console.log("userInfo already loaded:", userInfo);
    }
  }, [userInfo, getUserInfo]);

  const navButtonStyle = ({ isActive }: { isActive: boolean }) =>
    (isActive
      ? "text-primary bg-white hover:bg-gray-200 dark:text-gray-900"
      : "text-white bg-primary hover:bg-gray-400 dark:text-gray-900") +
    " button inline-block h-10 rounded-md p-2 text-center align-middle w-36 text-lg box-border shadow transition";

  return (
    <>
      <div className="h-20 bg-primary w-full block fixed z-10">
        <div className="w-full flex justify-between items-center px-10 top-10 absolute">
          <img src={logo} alt="logo" className="h-20 top-5 aspect-auto" />
          <NavLink to="/profile">
            {avatarUrl ? (
              <img
                src={avatarUrl}
                alt="user avatar"
                className="w-10 h-10 rounded-full object-cover"
              />
            ) : (
              <div className="w-10 h-10 rounded-full bg-gray-300 animate-pulse" />
            )}
          </NavLink>
        </div>

        <div className="flex space-x-4 w-2/3 left-1/6 gap-1 justify-center items-center top-10 h-20 absolute">
          {centerMenuLinks.map((link) => (
            <NavLink to={link.url} key={link.text} className={navButtonStyle}>
              {link.text}
            </NavLink>
          ))}
        </div>
      </div>

      <div className="w-full min-h-full bg-muted absolute top-20">
        <Outlet />
      </div>

      <ThemeToggle />
    </>
  );
}
