import { NavLink, Outlet } from "react-router-dom";
import logo from "@/assets/logo.webp";
import avatar from "@/assets/avatar.png";

// to add more centerMenuLinks edit here
const centerMenuLinks = [
  { text: "Home", url: "/homepage" },
  { text: "Search", url: "/search" },
];

export function Navbar() {
  const navButtonStyle = ({ isActive }: { isActive: boolean }) =>
    (isActive
      ? "text-primary bg-white hover:bg-gray-200"
      : "text-white bg-primary hover:bg-gray-400") +
    " button inline-block h-10 rounded-md p-2 text-center align-middle w-36 text-lg box-border transition";

  return (
    <>
      <div className={"h-20 bg-primary w-full block fixed"}>
        <div
          className={
            "w-full flex justify-between items-center px-10 top-10 absolute "
          }
        >
          <img src={logo} alt="logo" className={"h-20 top-5 aspect-auto"} />
          <NavLink to="/profile">
            <img
              src={avatar}
              alt="user picture"
              className={"w-10 h-10 rounded-full"}
            />
          </NavLink>
        </div>

        <div className="flex space-x-4 w-2/3 left-1/6 gap-1 justify-center items-center top-10 h-20 absolute ">
          {centerMenuLinks.map((link) => (
            <NavLink to={link.url} className={navButtonStyle}>
              {link.text}
            </NavLink>
          ))}
        </div>
      </div>

      <Outlet />
    </>
  );
}
