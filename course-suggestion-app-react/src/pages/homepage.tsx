import axiosInstance from "../api/axiosInstance";
import { useAuthStore } from "@/store/authStore.ts";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import openAEye from "@/assets/OpenAEye.png";
import Spinner from "@/components/ui/spinner";
import UploadCertificate from "@/components/uploadCertificate";
import { useUserDataStore } from "@/store/userDataStore";
import { UserInfo } from "@/store/userDataStore";

import ThemeToggle from "@/components/themeToggle";


interface PassedCourse {
  courseId: number;
  courseName: string;
  courseLevel: string;
  courseType: string;
  courseGrade: string;
}

export default function Homepage() {
  const navigate = useNavigate();
  const [passedCourses, setPassedCourses] = useState<PassedCourse[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const userDataStore = useUserDataStore();
  const userId = useAuthStore((s) => s.userId);
  const handleButtonClick = () => {
    navigate("/recommend-courses");
  };

  const fetchPassedCourses = async () => {
    if (userId === null) return;
  
    try {
      setLoading(true);
      const response = await axiosInstance.get(
        `/courses/passed-courses/user/${userId}`
      );
      setPassedCourses(response.data);
    } catch (error) {
      console.error("Failed to fetch passed courses", error);
    } finally {
      setLoading(false);
    }
  };
  

  useEffect(() => {
    const fetchData = async () => {
      if (userId !== null) {
        try {
          await fetchPassedCourses();
          const fullUser = await userDataStore.getUserInfo(userId);
          setUserInfo(fullUser); 
        } catch (err) {
          console.error("Error loading data:", err);
        }
      }
    };
  
    fetchData();
  }, [userId]);
  

  return (
    <div className="flex min-h-svh w-full flex-col items-center justify-center p-6 md:p-10">
      <ThemeToggle />
      {loading ? (
        <Spinner />
      ) : passedCourses.length === 0 ? (
        <div className="text-black text-center mt-6 dark:text-white">
          <p>No enough data about your courses...</p>
          <p>First upload your certificate of passing exams!</p>
          <br/>
       
              {userInfo && (
        <UploadCertificate
          user={userInfo}
          onSubmit={() => console.log("Certificate uploaded")}
        />
      )}

          
        </div>
      ) : (
        <div className="w-full md:w-8/9 lg:w-3/4 xl:w-2/3 mt-10 p-6 rounded-2xl">
          <h1 className="text-3xl mb-10 text-center text-black dark:text-white">
            Your Passed Courses
          </h1>
          <div className="space-y-8">
            <div>
              <div className="bg-white p-3 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                <div className="overflow-x-auto">
                  <table className="text-black min-w-full table-auto border-collapse border-none dark:text-white">
                    <thead>
                      <tr className="bg-white dark:bg-gray-700">
                        <th className="px-4 py-2 border-b border-gray-300 w-[10%] dark:border-gray-600 w-[10%]">
                          #
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[50%] dark:border-gray-600 w-[50%]">
                          Course Name
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[15%] dark:border-gray-600 w-[15%]">
                          Course Level
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[15%] dark:border-gray-600 w-[15%]">
                          Grade
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[15%] dark:border-gray-600 w-[15%]">
                          Type
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {passedCourses.map((course, index) => (
                        <tr
                          key={course.courseId}
                          className="text-center bg-white cursor-pointer hover:bg-gray-100 dark:bg-gray-900 dark:hover:bg-gray-700"
                        >
                          <td className="px-4 py-2">{index + 1}</td>
                          <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                            <Link to={`/course/${course.courseId}`}>{course.courseName}</Link>
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                            {course.courseLevel}
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                            {course.courseGrade}
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
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
                    className="bg-primary text-white rounded-full py-2 px-6 text-lg hover:shadow-2xl transition duration-200 flex items-center space-x-2 cursor-pointer dark:bg-gray-800 dark:text-white"
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
