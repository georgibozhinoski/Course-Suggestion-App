import openAEye from "@/assets/OpenAEye.png";

export default function RecommendCourses() {
  return (
    <div
      className="text-black p-6 pt-32 mx-[20%] pb-10 dark:text-white"
      style={{ height: "100%" }}
    >
      <h1 className=" text-center text-3xl mb-6 dark:text-white">Recommend Elective Courses</h1>
      <div className="bg-white p-7 mt-6 rounded-2xl shadow-[0px_0px_10px_rgba(0,0,0,0.25)] dark:bg-gray-900">
        <div className="flex justify-around space-x-6 border-gray-300 dark:border-gray-600">
          <div className="flex flex-col items-center space-y-2">
            <label className="text-lg font-medium">Select Semester</label>
            <div className="flex space-x-4">
              <div>
                <input
                  type="radio"
                  id="all-semester"
                  name="semester"
                  value="all"
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
                  className="mr-2"
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
          <button className="bg-primary text-white rounded-full py-2 px-6 text-lg hover:shadow-2xl transition duration-200 flex items-center space-x-2 cursor-pointer dark:bg-gray-600 dark:text-white">
            <div className="pr-3">Generate list</div>
            <img src={openAEye} alt="openAEye" className="w-12 h-12" />
          </button>
        </div>
      </div>
    </div>
  );
}
