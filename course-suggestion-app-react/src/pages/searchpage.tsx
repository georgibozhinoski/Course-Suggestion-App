import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";

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
  const [userId, setUserId] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

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

  const fetchUserId = () => {
    // const storedUserId = localStorage.getItem("userId");
    // if (storedUserId) {
    //   setUserId(parseInt(storedUserId));
    // } else {
    //   console.error("User ID not found.");
    // }
    setUserId(2);
  };

  const fetchUserMajorId = async () => {
    if (userId) {
      try {
        const response = await axiosInstance.get(
          `http://localhost:9090/api/v1/study-major/${userId}`
        );
        setMajorId(response.data);
      } catch (error) {
        console.error("Error fetching user major ID", error);
      }
    }
  };

  const fetchElectiveCourses = async () => {
    if (majorId === null) return;

    const electiveCoursesWithAllLevels: ElectiveCourses[] = [];

    for (let levelNo = 1; levelNo <= 3; levelNo++) {
      try {
        const response = await axiosInstance.get(
          `http://localhost:9090/api/v1/courses/by-major/${majorId}/elective/${levelNo}`
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
    fetchUserId();
  }, []);

  useEffect(() => {
    if (userId) {
      fetchUserMajorId();
    }
  }, [userId]);

  useEffect(() => {
    const fetchCourses = async () => {
      if (majorId === null) return;

      const coursesForAllSemesters: SemesterCourses[] = [];

      for (let semesterNo = 1; semesterNo <= 8; semesterNo++) {
        try {
          const response = await axiosInstance.get(
            `http://localhost:9090/api/v1/courses/by-major/${majorId}/semester/${semesterNo}`
          );
          coursesForAllSemesters.push({
            semesterNo,
            courses: response.data,
          });
        } catch (error) {
          console.error(
            `Failed to fetch courses for semester ${semesterNo}`,
            error
          );
        }
      }

      setSemesterCourses(coursesForAllSemesters);
      fetchElectiveCourses();
    };

    fetchCourses();
  }, [majorId]);

  return (
    <div className="pt-32 text-center mx-[15%] pb-10">
      {loading ? (
        <div className="flex justify-center items-center h-[100vh] bg-white">
          <div className="flex items-center space-x-2">
            <div className="w-8 h-8 border-4 border-t-4 border-gray-700 rounded-full animate-spin"></div>
            <div className="text-xl text-gray-700 font-semibold">
              Loading...
            </div>
          </div>
        </div>
      ) : (
        <>
          <h1 className="text-black text-3xl mb-6">Courses by Semester</h1>
          <div className="space-y-8">
            {semesterCourses.map((semesterData) => (
              <div key={semesterData.semesterNo}>
                <h2
                  className="text-2xl  mb-4 text-left"
                  style={{ marginLeft: "-30px" }}
                >
                  <span className="text-black">
                    {semesterNames[semesterData.semesterNo - 1]}
                  </span>
                </h2>
                <div className="bg-white p-3 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                  <div className="overflow-x-auto">
                    <table className="text-black min-w-full table-auto border-collapse border-none">
                      <thead>
                        <tr className="bg-white">
                          <th className="px-4 py-2 border-b border-gray-300 w-[10%]">
                            #
                          </th>
                          <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[70%]">
                            Course Name
                          </th>
                          <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[20%]">
                            Course Level
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        {semesterData.courses.map((course, index) => (
                          <tr
                            key={course.courseId}
                            className="bg-white cursor-pointer hover:bg-gray-100"
                          >
                            <td className="px-4 py-2">{index + 1}</td>
                            <td className="text-left px-4 py-2 border-l border-gray-300">
                              {course.courseName}
                            </td>
                            <td className="text-left px-4 py-2 border-l border-gray-300">
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
                <span className="text-black">Elective Courses</span>
              </h2>
              {electiveCourses.map((electiveData) => (
                <div key={electiveData.levelNo}>
                  <h3 className="text-xl mb-4 text-center mt-4 text-black">
                    Level {electiveData.levelNo}
                  </h3>
                  <div className="bg-white p-3 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                    <div className="overflow-x-auto">
                      <table className="text-black min-w-full table-auto border-collapse border-none">
                        <thead>
                          <tr className="bg-white">
                            <th className="px-4 py-2 border-b border-gray-300 w-[10%]">
                              #
                            </th>
                            <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[70%]">
                              Course Name
                            </th>
                            <th className="text-left px-4 py-2 border-b border-l border-gray-300 w-[20%]">
                              Course Level
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {electiveData.courses.map((course, index) => (
                            <tr
                              key={course.courseId}
                              className="bg-white cursor-pointer hover:bg-gray-100"
                            >
                              <td className="px-4 py-2">{index + 1}</td>
                              <td className="text-left px-4 py-2 border-l border-gray-300">
                                {course.courseName}
                              </td>
                              <td className="text-left px-4 py-2 border-l border-gray-300">
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
      )}
    </div>
  );
}
