import { useParams } from "react-router";
import axiosInstance from "../api/axiosInstance";
import { useState, useEffect } from "react";
import { useAuthStore } from "@/store/authStore";
import Spinner from "@/components/ui/spinner";
import { UserInfo } from "@/store/userDataStore";
import { useUserDataStore } from "@/store/userDataStore";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import avatar from "@/assets/avatar.png";
import { Course, Comment, CheatSheet, CreateCheatSheetDTO } from "@/lib/interfaces";
import RatingCard from "@/components/RatingCard.tsx";




const ThreadPage = () => {
  const { id } = useParams();
  const [thread, setThread] = useState<Course | undefined>();
  const [loading, setLoading] = useState<boolean>(false);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const userId = useAuthStore((s) => s.userId);
  const userDataStore = useUserDataStore();
  const [comments, setComments] = useState<Comment[]>([]);
  const [comment, setComment] = useState('');
  const [cheatSheets, setCheatSheets] = useState<CheatSheet[]>([]);
  const [newCheatSheet, setNewCheatSheet] = useState<CreateCheatSheetDTO>({
    sheetName: "",
    courseId: undefined,
    file: "",
    sheetContent: "",
    sheetDate: "",
    userId: undefined
  });

  const token = localStorage.getItem('token');

  const fetchCourse = async () => {
    if (userId === null) return;

    try {
      setLoading(true);
      const response = await axiosInstance.get(`/courses/details/${id}`);
      setThread(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("Failed to fetch course", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchComments = async () => {
    if (userId === null) return;

    try {
      setLoading(true);
      const response = await axiosInstance.get(`/comments/course/${id}`);
      setComments(response.data);
    } catch (error) {
      console.error("Failed to fetch course", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCheatSheets = async () => {
    if (userId === null) return;

    try {
      setLoading(true);
      const response = await axiosInstance.get(`/cheatSheets/course/${id}`); //todo replace
      setCheatSheets(response.data);
    } catch (error) {
      setCheatSheets([]);
      console.error("Failed to fetch cheat sheets", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      if (userId !== null) {
        try {
          await fetchCourse();
          await fetchComments();
          await fetchCheatSheets();
          const fullUser = await userDataStore.getUserInfo(userId);
          setUserInfo(fullUser);
        } catch (err) {
          console.error("Error loading data:", err);
        }
      }
    };

    fetchData();
  }, [userId]);

  useEffect(() => {
    if (thread) {
      console.log("Thread updated:", thread);
    }
  }, [thread]);

  useEffect(() => {
   setNewCheatSheet({
     ...newCheatSheet,
     userId: userId,
     courseId: id?+id:undefined
   })
  }, [userId, id]);

  function formatCustomDate(input: string): string {
    const cleanInput = input.split(".")[0];
    const date = new Date(cleanInput);

    const hours = date.getHours();
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const ampm = hours >= 12 ? "pm" : "am";
    const hour12 = hours % 12 === 0 ? 12 : hours % 12;

    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = String(date.getFullYear()).slice(-2);

    return `${hour12}:${minutes}${ampm} ${day}.${month}.${year}`;
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setComment(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (comment.trim() === '') return;

    try {


        await axiosInstance.post(
            `/comments/course/${id}`,
            { commentContent: comment },
            {
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
              },
              withCredentials: true,
            }
          );


        fetchComments();
        setComment('');
      } catch (error) {
        console.error('Failed to post comment:', error);
      }
  };

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
      const file = e.target.files?.[0];
      if (!file) return;
      const reader = new FileReader();
      reader.onload = () => {
        setNewCheatSheet({
          ...newCheatSheet,
          file: reader.result as string,
          sheetDate: new Date().toISOString(),
        });
      };
      reader.readAsDataURL(file);
  }

  const handleCheatSheetUpload = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      await axiosInstance.post("/cheatSheets", newCheatSheet);
      setNewCheatSheet({
        sheetName: "",
        sheetContent: "",
        sheetDate: "",
        file: "",
        courseId: id ? +id : undefined,
        userId: userId,
      });
      fetchCheatSheets();
    } catch (err) {
      console.error("Failed to upload cheat sheet", err);
    }
  }

  const handleCheatSheetDownload = (sheet: CheatSheet)=> {

  }

  return (
    <>
      {loading ? (
        <Spinner />
      ) : !thread ? (
        <div className="text-black text-center mt-6 dark:text-white">
          <p>No enough data about your courses...</p>
          <p>First upload your certificate of passing exams!</p>
          <br />
        </div>
      ) : thread ? (
        <div className="container mx-auto">
          <div className="flex items-center justify-center mt-18 mb-14 text-black text-3xl dark:text-white">
            <p>{thread.courseName}</p>
          </div>
          <div className="flex items-stretch gap-x-4">
            <div className="w-1/2 flex flex-col">
              <p className="text-black mb-4 text-xl dark:text-white">Course Details</p>
              <Card className={'p-10 flex-1'}>
                <CardContent className="border-b border-gray-400 flex items-center">
                  <span className="min-w-[100px] border-r border-gray-400 pr-4">
                    Code
                  </span>
                  <span className="pl-4">{thread.courseCode}</span>
                </CardContent>

                <CardContent className="border-b border-gray-400 flex items-center">
                  <span className="min-w-[100px] border-r border-gray-400 pr-4">
                    Credits
                  </span>
                  <span className="pl-4">{thread.creditScore}</span>
                </CardContent>
                <CardContent className="border-b border-gray-400 flex items-center">
                  <span className="min-w-[100px] border-r border-gray-400 pr-4">
                    Semester
                  </span>
                  <span className="pl-4">
                    {thread.winter ? "Winter" : "Summer"}
                  </span>
                </CardContent>
                <CardContent className="border-b border-gray-400 flex items-center">
                  <span className="min-w-[100px]  pr-4">Description</span>
                  <span className="pl-4 border-l border-gray-400">
                    {thread.courseDescription}
                  </span>
                </CardContent>
                <CardContent className="flex items-center">
                  <span className="min-w-[100px]  pr-4">Level</span>
                  <span className="pl-4 border-l border-gray-400">
                    {thread.courseLevel}
                  </span>
                </CardContent>
              </Card>
            </div>
            <div className="w-1/2 flex flex-col">
              <p className="text-black text-xl mb-4 w-full dark:text-white">
                 Opportunities after completing this course
              </p>
              <Card className={'flex-1 w-full mb-4'}>
                <CardContent>
                  {thread.courseGoals}
                </CardContent>
              </Card>

              <p className="text-black text-xl mb-4 w-full dark:text-white">
                Rating
              </p>
              <RatingCard className={'flex-1 w-full h-30'} course={thread} />
          </div>
          </div>
          <div className="flex gap-x-4">
            <div className="w-1/2">
              <p className="text-black my-4 text-xl dark:text-white">Comments</p>
              <Card>
                <form  onSubmit={handleSubmit}>
                  <input
                    type="text"
                    value={comment}
                    name="commentContent"
                    onChange={handleChange}
                    placeholder="Add Comment"
                    className="rounded-md px-4 py-2 focus:outline-none w-full"
                  />
                  <div className="text-center">
                    <Button type="submit" className="w-1/2">Submit</Button>
                  </div>
                </form>
              </Card>
              {comments.map((comment) => {
                return (
                  <Card className="mt-4" key={comment.commentID}>
                    <div className="flex justify-between">
                      <div className="flex items-center ml-8">
                        <img
                          src={avatar}
                          alt="user picture"
                          className="w-10 h-10 rounded-full"
                        />
                        <p className="ml-4">{comment.authorName}</p>
                      </div>

                      <small className="mr-8">
                        {formatCustomDate(comment.commentDate)}
                      </small>
                    </div>

                    <p className="ml-22">
                      {(() => {
                        try {
                          return JSON.parse(comment.commentContent)
                            .commentContent;
                        } catch {
                          return comment.commentContent; // fallback if it's already plain text
                        }
                      })()}
                    </p>
                  </Card>
                );
              })}
            </div>
            <div className="w-1/2">
              <p className="text-black my-4 text-xl dark:text-white">Cheat sheets</p>
              <Card>
                <CardContent>
                  <div className={'flex'}>
                    <input
                        type="text"
                        value={newCheatSheet.sheetName}
                        name="cheatSheetName"
                        onChange={(e) =>
                            setNewCheatSheet({ ...newCheatSheet, sheetName: e.target.value })
                        }
                        placeholder="Sheet Name"
                        className="rounded-md px-4 py-2 focus:outline-none w-1/2 inline mb-4"
                    />

                    <input
                        type="file"
                        name="cheatSheetFile"
                        onChange={handleFileUpload}
                        className="inline w-1/2 text-sm text-gray-700 dark:text-gray-300 file:mr-4 file:py-2 file:px-4
                                    file:rounded-md file:border-0
                                    file:text-sm file:font-semibold
                                    file:bg-primary file:text-white
                                    hover:file:bg-blue-600
                                    cursor-pointer"
                    />
                  </div>

                  <div className="text-center">
                    <Button type="button" className="w-1/2" onClick={handleCheatSheetUpload}>
                      Upload
                    </Button>
                  </div>
                </CardContent>
              </Card>
              {cheatSheets.map((sheet: CheatSheet) =>
                    <Card className="mt-4" key={sheet.sheetId}>
                      <div className="flex justify-between">
                        <small className="mr-8">
                          {formatCustomDate(sheet.sheetDate)}
                        </small>
                      </div>
                      <h3 className="ml-22">
                        {sheet.sheetName}
                      </h3>
                      <button onClick={() => {handleCheatSheetDownload(sheet)}}>
                        Download
                      </button>
                    </Card>
                )}
            </div>
          </div>
        </div>
      ) : (
        <p>Course not found or failed to load.</p>
      )}
    </>
  );
};

export default ThreadPage;
