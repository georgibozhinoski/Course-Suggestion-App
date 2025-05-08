import { useEffect, useState } from "react";

export default function ThemeToggle() {
    const [isDark, setIsDark] = useState(() => localStorage.getItem("theme") === "dark");
  
    useEffect(() => {
      const root = window.document.documentElement;
      if (isDark) {
        root.classList.add("dark");
        localStorage.setItem("theme", "dark");
      } else {
        root.classList.remove("dark");
        localStorage.setItem("theme", "light");
      }
    }, [isDark]);
  
    return (
      <div className="fixed bottom-6 right-6 z-50">
        <button
          onClick={() => setIsDark((prev) => !prev)}
          className="w-12 h-12 flex items-center justify-center rounded-full bg-gray-300 dark:bg-gray-500 text-xl shadow-lg hover:scale-105 transition"
          title="Toggle Theme"
        >
          {isDark ? "☀️" : "🌙"}
        </button>
      </div>
    );
  }
  