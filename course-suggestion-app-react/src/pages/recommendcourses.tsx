import openAEye from "@/assets/OpenAEye.png";
import {useEffect, useState} from "react";
import {useAuthStore} from "@/store/authStore.ts";
import axiosInstance from "@/api/axiosInstance.ts";
import Spinner from "@/components/ui/spinner.tsx";
import {useNavigate} from "react-router";

interface pyRecDTO {
    course: {
        courseName: string;
        courseId: number;
        courseLevel: string;
    },
    extraData: string;
}

export default function RecommendCourses() {
    const [formData, setFormData] = useState({
        semester: "ALL",
        level: "ALL",
        reason: "",
        userId: undefined as number | undefined,
    })
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [courses, setCourses] = useState([]);

    const navigate = useNavigate();
    const authStore = useAuthStore();

    useEffect(() => {
        authStore.userId && setFormData({...formData, userId: authStore.userId});
    }, []);

    const patchFormData = (key: string, value: string) =>
        setFormData({...formData, [key]: value});

    const generateList = async () => {
        setCourses([]);
        setLoading(true);
        const response = await axiosInstance.post("/recommendations", formData);

        if (response.status === 200) {
            console.log(response.data);
            setCourses(response.data)
        } else {
            console.error(response.data);
            setError(response.data.message);
        }
        setLoading(false);
    }


    return (
        <div className="text-black p-6 pt-32 mx-[20%] pb-10 min-h-full">
            <h1 className=" text-center text-3xl mb-6">Recommend Elective Courses</h1>
            <div className="bg-white p-7 mt-6 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)]">
                <div className="flex justify-around space-x-6 border-gray-300">
                    <div className="flex flex-col items-center space-y-2">
                        <label className="text-lg font-medium">Select Semester</label>
                        <div className="flex space-x-4">
                            <div>
                                <input
                                    type="radio"
                                    id="all-semester"
                                    name="semester"
                                    value="ALL"
                                    className="mr-2"
                                    aria-selected={formData.semester === "ALL"}
                                    defaultChecked={true}
                                    onChange={(e) => patchFormData("semester", e.target.value)}
                                />
                                <label htmlFor="all-semester">All</label>
                            </div>
                            <div>
                                <input
                                    type="radio"
                                    id="winter"
                                    name="semester"
                                    value="WINTER"
                                    className="mr-2"
                                    aria-selected={formData.semester === "WINTER"}
                                    onChange={(e) => patchFormData("semester", e.target.value)}
                                />
                                <label htmlFor="winter">Winter</label>
                            </div>
                            <div>
                                <input
                                    type="radio"
                                    id="summer"
                                    name="semester"
                                    value="SUMMER"
                                    className="mr-2"
                                    aria-selected={formData.semester === "SUMMER"}
                                    onChange={(e) => patchFormData("semester", e.target.value)}
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
                                    value="ALL"
                                    className="mr-2"
                                    aria-selected={formData.level === "ALL"}
                                    defaultChecked={true}
                                    onChange={(e) => patchFormData("level", e.target.value)}
                                />
                                <label htmlFor="all-level">All</label>
                            </div>
                            <div>
                                <input
                                    type="radio"
                                    id="level-1"
                                    name="level"
                                    value="1"
                                    className="mr-2"
                                    aria-selected={formData.level === "L1"}
                                    onChange={(e) => patchFormData("level", e.target.value)}
                                />
                                <label htmlFor="level-1">L1</label>
                            </div>
                            <div>
                                <input
                                    type="radio"
                                    id="level-2"
                                    name="level"
                                    value="2"
                                    className="mr-2"
                                    aria-selected={formData.level === "L2"}
                                    onChange={(e) => patchFormData("level", e.target.value)}
                                />
                                <label htmlFor="level-2">L2</label>
                            </div>
                            <div>
                                <input
                                    type="radio"
                                    id="level-3"
                                    name="level"
                                    value="3"
                                    className="mr-2"
                                    aria-selected={formData.level === "L3"}
                                    onChange={(e) => patchFormData("level", e.target.value)}
                                />
                                <label htmlFor="level-3">L3</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="mt-6">
                    <label className="block text-lg font-medium mb-2">
                        Give your reasons
                    </label>
                    <textarea
                        placeholder="Input more details (optional)"
                        className="w-full p-3 rounded-lg border border-gray-300 resize-y"
                        rows={4}
                    />
                </div>

                <div className="flex justify-center mt-10 mb-5">
                    <button
                        className="bg-primary text-white rounded-full py-2 px-6 text-lg hover:shadow-2xl transition duration-200 flex items-center space-x-2 cursor-pointer"
                        onClick={generateList}
                    >
                        <div className="pr-3">Generate list</div>
                        <img src={openAEye} alt="openAEye" className="w-12 h-12"/>
                    </button>
                </div>
            </div>

            {courses.length > 0 && <div>
                <h1 className={"text-center text-3xl m-6 text-display"}>Recommended courses</h1>
                {courses.map((course: pyRecDTO, index) =>
                    <div key={index}
                         className="max-w-4xl mx-auto my-5 p-6 flex items-start gap-4 bg-white shadow-md rounded-2xl"
                         onClick={() => navigate(`/course/${course.course.courseId}`)}
                    >

                        <div className="w-10 flex flex-col items-center justify-center text-white text-lg font-bold">
                            <img src={openAEye} alt="openAEye" className="w-10 h-10 " />
                            <div className="text-xl text-black text-bold font-light">{index+1}</div>
                        </div>

                        <div className="flex-1">
                            <div className="text-lg font-medium text-gray-800">{course.course.courseName}</div>
                            <p className="italic text-gray-600 mt-1">
                                Similarity score: {(+course.extraData * 100).toFixed(2)}
                            </p>
                        </div>

                        <div className="w-10 flex flex-col items-center justify-center text-white text-lg font-bold">
                            <div className="text-xl text-black text-bold font-light">L{course.course.courseLevel}</div>
                        </div>

                    </div>)}
            </div>}

            {loading && <div className={'p-20 flex justify-center items-center'}>
                <Spinner/>
            </div>}

        </div>
    );
}
