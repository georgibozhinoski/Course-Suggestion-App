import axiosInstance from "../api/axiosInstance";
import { useAuthStore } from "@/store/authStore.ts";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import openAEye from "@/assets/OpenAEye.png";

interface PassedCourse {
  courseId: number;
  courseName: string;
  courseLevel: string;
  courseType: string;
  courseGrade: string;
}

export default function Homepage() {
  const logout = useAuthStore((s) => s.logout);
  const navigate = useNavigate();
  const [passedCourses, setPassedCourses] = useState<PassedCourse[]>([]);
  const [userId, setUserId] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const handleLogout = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      logout();
      navigate("/login");
    } catch {
      alert("Logout failed");
    }
  };

  const handleButtonClick = () => {
    navigate("/recommend-courses");
  };

  const fetchUserId = () => {
    // Example: fetching from localStorage if you store userId there
    // const storedUserId = localStorage.getItem("userId");
    // if (storedUserId) {
    //   setUserId(parseInt(storedUserId));
    // }
    setUserId(2); // Temporary hardcoded
  };

  const fetchPassedCourses = async () => {
    if (userId === null) return;

    try {
      const response = await axiosInstance.get(
        `http://localhost:9090/api/v1/courses/passed-courses/user/${userId}`
      );
      setPassedCourses(response.data);
    } catch (error) {
      console.error("Failed to fetch passed courses", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserId();
  }, []);

  useEffect(() => {
    if (userId !== null) {
      fetchPassedCourses();
    }
  }, [userId]);

  return (
    <div className="flex min-h-svh w-full flex-col items-center justify-center p-6 md:p-10">
      <button onClick={handleLogout}>Logout</button>

      {loading ? (
        <div className="flex justify-center items-center h-[100vh] bg-white">
          <div className="flex items-center space-x-2">
            <div className="w-8 h-8 border-4 border-t-4 border-gray-700 rounded-full animate-spin"></div>
            <div className="text-xl text-gray-700 font-semibold">
              Loading...
            </div>
          </div>
        </div>
      ) : passedCourses.length === 0 ? (
        <div className="text-black text-center mt-6">
          <p>No enough data about your courses...</p>
          <p>First upload your certificate of passing exams!</p>
        </div>
      ) : (
        <div className="w-full md:w-8/9 lg:w-3/4 xl:w-2/3 mt-15 bg-white p-6 rounded-2xl">
          <h1 className="text-3xl mb-10 text-center text-black">
            Your Passed Courses
          </h1>
          <div className="space-y-8">
            <div>
              <div className="bg-white p-3 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                <div className="overflow-x-auto">
                  <table className="text-black min-w-full table-auto border-collapse border-none">
                    <thead>
                      <tr className="bg-white">
                        <th className="px-4 py-2 border-b border-gray-300 w-[10%]">
                          #
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[50%]">
                          Course Name
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[15%]">
                          Course Level
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[15%]">
                          Grade
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[15%]">
                          Type
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {passedCourses.map((course, index) => (
                        <tr
                          key={course.courseId}
                          className="text-center bg-white cursor-pointer hover:bg-gray-100"
                        >
                          <td className="px-4 py-2">{index + 1}</td>
                          <td className="text-left px-4 py-2 border-l border-gray-300">
                            {course.courseName}
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300">
                            {course.courseLevel}
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300">
                            {course.courseGrade}
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300">
                            {course.courseType}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                <div className="flex justify-center mt-10 mb-10">
                  <button
                    onClick={handleButtonClick}
                    className="bg-primary text-white rounded-full py-2 px-6 text-lg hover:shadow-2xl transition duration-200 flex items-center space-x-2 cursor-pointer"
                  >
                    <div className="pr-3">Recommend elective courses</div>
                    <img src={openAEye} alt="openAEye" className="w-12 h-12" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
