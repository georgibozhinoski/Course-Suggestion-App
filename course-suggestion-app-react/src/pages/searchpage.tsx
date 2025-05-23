import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";
import Spinner from "@/components/ui/spinner.tsx";
import { useAuthStore } from "@/store/authStore.ts";
import {Link, useNavigate} from "react-router";

interface Course {
  courseId: number;
  courseName: string;
  courseLevel: string;
}

interface SemesterCourses {
  semesterNo: number;
  courses: Course[];
}

interface ElectiveCourses {
  levelNo: number;
  courses: Course[];
}

export default function SearchPage() {
  const [semesterCourses, setSemesterCourses] = useState<SemesterCourses[]>([]);
  const [electiveCourses, setElectiveCourses] = useState<ElectiveCourses[]>([]);
  const [majorId, setMajorId] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const userId = useAuthStore((s) => s.userId);
  const navigate = useNavigate();


  const semesterNames: string[] = [
    "First Semester",
    "Second Semester",
    "Third Semester",
    "Fourth Semester",
    "Fifth Semester",
    "Sixth Semester",
    "Seventh Semester",
    "Eighth Semester",
  ];

  const fetchUserMajorId = async () => {
    if (userId) {
      try {
        setLoading(true);
        await axiosInstance
          .get(`/study-major/${userId}`)
          .then((response) => {
            setMajorId(response.data);
            setLoading(false);
          });
      } catch (error) {
        console.error("Error fetching user major ID", error);
        setLoading(false);
      }
    }
  };

  const fetchElectiveCourses = async () => {
    if (majorId === null) return;

    const electiveCoursesWithAllLevels: ElectiveCourses[] = [];

    setLoading(true);
    for (let levelNo = 1; levelNo <= 3; levelNo++) {
      try {
        const response = await axiosInstance.get(
          `courses/by-major/${majorId}/elective/${levelNo}`
        );
        electiveCoursesWithAllLevels.push({
          levelNo,
          courses: response.data,
        });
      } catch (error) {
        console.error(`Failed to fetch courses for semester ${levelNo}`, error);
      }
    }
    setElectiveCourses(electiveCoursesWithAllLevels);
    setLoading(false);
  };

  useEffect(() => {
    if (userId) {
      fetchUserMajorId();
    }
  }, [userId]);

  useEffect(() => {
    const fetchCourses = async () => {
      if (majorId === null) return;

      const coursesForAllSemesters: SemesterCourses[] = [];

      try {
        setLoading(true);
        const response = await axiosInstance.get(
          `/courses/by-major/${majorId}/all`
        );

        Object.entries(response.data as Record<string, Course[]>).forEach(
          ([semesterNo, courses]: [string, Course[]]) => {
            coursesForAllSemesters.push({
              semesterNo: +semesterNo,
              courses,
            });
          }
        );
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }

      setSemesterCourses(coursesForAllSemesters);
      fetchElectiveCourses();
    };

    fetchCourses();
  }, [majorId]);

  return (
    <div className="pt-25 text-center mx-[15%] pb-10 min-h-svh">
      <>
        <h1 className="text-black text-3xl mb-6 dark:text-white">Courses by Semester</h1>
        <div className="space-y-8">
          {semesterCourses.map((semesterData) => (
            <div key={semesterData.semesterNo}>
              <h2
                className="text-2xl  mb-4 text-left"
                style={{ marginLeft: "-30px" }}
              >
                <span className="text-black dark:text-white">
                  {semesterNames[semesterData.semesterNo - 1]}
                </span>
              </h2>
              <div className="bg-white p-3 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                <div className="overflow-x-auto">
                  <table className="text-black min-w-full table-auto border-collapse border-none dark:text-white">
                    <thead>
                      <tr className="bg-white dark:bg-gray-700">
                        <th className="px-4 py-2 border-b border-gray-300 w-[10%] dark:border-gray-600 w-[10%]">
                          #
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[70%] dark:border-gray-600 w-[70%]">
                          Course Name
                        </th>
                        <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[20%] dark:border-gray-600 w-[20%]">
                          Course Level
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {semesterData.courses.map((course, index) => (
                        <tr
                          key={course.courseId}
                          className="bg-white cursor-pointer hover:bg-gray-100 dark:bg-gray-900 dark:hover:bg-gray-700"
                          onClick={() => navigate(`/course/${course.courseId}`)}
                        >
                          <td className="px-4 py-2">{index + 1}</td>
                          <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                            {course.courseName}
                          </td>
                          <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                            L{course.courseLevel}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          ))}

          <div>
            <h2
              className="text-2xl mb-4 text-left"
              style={{ marginLeft: "-30px" }}
            >
              <span className="text-black dark:text-white">Elective Courses</span>
            </h2>
            {electiveCourses.map((electiveData) => (
              <div key={electiveData.levelNo}>
                <h3 className="text-xl mb-4 text-center mt-4 text-black dark:text-white">
                  Level {electiveData.levelNo}
                </h3>
                <div className="bg-white p-3 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                  <div className="overflow-x-auto">
                    <table className="text-black min-w-full table-auto border-collapse border-none dark:text-white">
                      <thead>
                        <tr className="bg-white dark:bg-gray-700">
                          <th className="px-4 py-2 border-b border-gray-300 w-[10%] dark:border-gray-600 w-[10%]">
                            #
                          </th>
                          <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[70%] dark:border-gray-600 w-[70%]">
                            Course Name
                          </th>
                          <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[20%] dark:border-gray-600 w-[20%]">
                            Course Level
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        {electiveData.courses.map((course, index) => (
                          <tr
                            key={course.courseId}
                            className="bg-white cursor-pointer hover:bg-gray-100 dark:bg-gray-900 dark:hover:bg-gray-700"
                            onClick={() => navigate(`/course/${course.courseId}`)}
                          >
                            <td className="px-4 py-2">{index + 1}</td>
                            <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                              {course.courseName}
                            </td>
                            <td className="text-left px-4 py-2 border-l border-gray-300 dark:border-gray-600">
                              L{course.courseLevel}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </>
      {loading && <Spinner />}
    </div>
  );
}
