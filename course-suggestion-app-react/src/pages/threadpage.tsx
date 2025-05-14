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
import file from "@/assets/file.svg";
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
      const response = await axiosInstance.get(`/cheatsheets/by-course/${id}`);
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

    setNewCheatSheet({
      ...newCheatSheet,
      file: file,
      sheetDate: new Date().toISOString(),
    });
  };


  const handleCheatSheetUpload = async (e: React.FormEvent<HTMLFormElement> | React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    try {
      const formData = new FormData();

      const date = new Date(newCheatSheet.sheetDate);
      const formattedDate = date.toISOString().split(".")[0];
      formData.append("sheetDate", formattedDate);

      formData.append("sheetContent", newCheatSheet.sheetName);
      formData.append("userId", String(newCheatSheet.userId));
      formData.append("courseId", String(newCheatSheet.courseId));
      if (newCheatSheet.file instanceof File) {
        formData.append("files", newCheatSheet.file);
      }

      await axiosInstance.post("/cheatsheets", formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        },
      });

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
  };


  const handleCheatSheetDownload = async (sheet: CheatSheet) => {
    try {
      const response = await axiosInstance.get(`/cheatsheets/file/${sheet.sheetId}`, {
        responseType: 'blob',
      });

      const blob = new Blob([response.data], { type: 'application/pdf' });
      console.log(response.data)

      const url = window.URL.createObjectURL(blob);

      const a = document.createElement('a');
      a.href = url;
      a.download = sheet.sheetName;
      document.body.appendChild(a);
      a.click();

      document.body.removeChild(a);
    } catch (error) {
      console.error('Error downloading the cheat sheet file', error);
    }
  };


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
        <div className="container mx-auto pb-20">
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
              <Card className={'h-40'}>
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
                  <Card className="mt-4 min-h-40" key={comment.commentID}>
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
              <Card className={'h-40'}>
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
                  <Card className="mt-4 transition-all min-h-40" key={sheet.sheetId}>
                    <CardContent>
                      <div className={'flex items-center justify-between'}>
                        <div className="flex p-5 justify-between items-center mb-3">
                          <small className="text-gray-500 text-sm">
                            {formatCustomDate(sheet.sheetDate)}
                          </small>
                        </div>
                        <div className={'flex'}>
                          <img
                              src={file}
                              alt="file icon"
                              className="w-8 h-8 inline"
                          />
                          <h3 className="text-xl font-semibold text-gray-800 mb-4">
                            {sheet.sheetName}
                          </h3>
                        </div>


                        <Button
                            variant="default"
                            className="w-1/3 py-2 px-4 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-all focus:outline-none focus:ring-2 focus:ring-blue-300"
                            onClick={() => handleCheatSheetDownload(sheet)}
                        >
                          Download
                        </Button>
                      </div>

                    </CardContent>

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
