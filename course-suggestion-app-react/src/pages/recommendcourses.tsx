import { useState } from "react";
import openAEye from "@/assets/OpenAEye.png";
import { useAuthStore } from "@/store/authStore";
import axiosInstance from "@/api/axiosInstance";

export default function RecommendCourses() {
  const [semester, setSemester] = useState("all");
  const [level, setLevel] = useState("all");
  const [reasons, setReasons] = useState("");
  const [recommendedCourses, setRecommendedCourses] = useState<string[] | null>(null);
  const [loading, setLoading] = useState(false);

  const authStore = useAuthStore();

  const handleSubmit = async () => {
    console.log("Generate list button clicked");
    setRecommendedCourses(null); 
    setLoading(true); 
  
    try {
      const response = await axiosInstance.post("/openai/recommend-courses", {
        semester,
        level,
        reasons,
        userId: authStore.userId,
      });
  
      console.log("Backend response:", response.data);
  
      if (response.data && typeof response.data.recommendations === "string") {
        const coursesArray = response.data.recommendations
          .split(", ")
          .map((course: string) => course.trim());
        setRecommendedCourses(coursesArray);
      } else {
        console.error("Error fetching recommended courses");
      }
    } catch (error) {
      console.error("Error submitting request", error);
    } finally {
      setLoading(false); 
    }
  };
  

  return (
    <div className="text-black p-6 pt-32 mx-[20%] pb-10" style={{ height: "100%" }}>
      <h1 className=" text-center text-3xl mb-6">Recommend Elective Courses</h1>
      <div className="bg-white p-7 mt-6 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
        <div className="flex justify-around space-x-6 border-gray-300">
          {/* Semester and Level Selection */}
          <div className="flex flex-col items-center space-y-2">
            <label className="text-lg font-medium">Select Semester</label>
            <div className="flex space-x-4">
              <div>
                <input
                  type="radio"
                  id="all-semester"
                  name="semester"
                  value="all"
                  checked={semester === "all"}
                  onChange={(e) => setSemester(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="all-semester">All</label>
              </div>
              <div>
                <input
                  type="radio"
                  id="winter"
                  name="semester"
                  value="winter"
                  checked={semester === "winter"}
                  onChange={(e) => setSemester(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="winter">Winter</label>
              </div>
              <div>
                <input
                  type="radio"
                  id="summer"
                  name="semester"
                  value="summer"
                  checked={semester === "summer"}
                  onChange={(e) => setSemester(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="summer">Summer</label>
              </div>
            </div>
          </div>

          <div className="flex flex-col items-center space-y-2">
            <label className="text-lg font-medium">Select Level</label>
            <div className="flex space-x-4">
              <div>
                <input
                  type="radio"
                  id="all-level"
                  name="level"
                  value="all"
                  checked={level === "all"}
                  onChange={(e) => setLevel(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="all-level">All</label>
              </div>
              <div>
                <input
                  type="radio"
                  id="level-1"
                  name="level"
                  value="L1"
                  checked={level === "L1"}
                  onChange={(e) => setLevel(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="level-1">L1</label>
              </div>
              <div>
                <input
                  type="radio"
                  id="level-2"
                  name="level"
                  value="L2"
                  checked={level === "L2"}
                  onChange={(e) => setLevel(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="level-2">L2</label>
              </div>
              <div>
                <input
                  type="radio"
                  id="level-3"
                  name="level"
                  value="L3"
                  checked={level === "L3"}
                  onChange={(e) => setLevel(e.target.value)}
                  className="mr-2"
                />
                <label htmlFor="level-3">L3</label>
              </div>
            </div>
          </div>
        </div>

        <div className="mt-6">
          <label className="block text-lg font-medium mb-2">Give your reasons</label>
          <textarea
            placeholder="Input more details (optional)"
            className="w-full p-3 rounded-lg border border-gray-300 resize-y"
            rows={4}
            value={reasons}
            onChange={(e) => setReasons(e.target.value)}
          />
        </div>

        <div className="flex justify-center mt-10 mb-5">
          <button
            className="bg-primary text-white rounded-full py-2 px-6 text-lg hover:shadow-2xl transition duration-200 flex items-center space-x-2 cursor-pointer"
            onClick={handleSubmit}
            disabled={loading} 
          >
            <div className="pr-3">Generate list</div>
            <img src={openAEye} alt="openAEye" className="w-12 h-12" />
          </button>
        </div>

        {/* Display the recommended courses as cards */}
        <div className="grid grid-cols-1 gap-4 mt-6">
          {recommendedCourses && recommendedCourses.length > 0 ? (
            recommendedCourses.map((course: string, index) => (
              <div key={index} className="bg-white p-4 rounded-lg shadow-lg">
                <h3 className="text-xl font-semibold">{course.split(" - ")[0]}</h3>
                {/* You can show the reason here */}
                <p className="text-sm text-gray-600">Reason: {course.split(" - ")[1]}</p>
              </div>
            ))
          ) : (
            <p>No recommended courses available yet. Please generate the list.</p>
          )}
        </div>
      </div>
    </div>
  );
}
