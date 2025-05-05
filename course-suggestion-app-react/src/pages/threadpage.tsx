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

interface Course {
  courseId: number;
  courseCode: string;
  courseName: string;
  courseLevel: string;
  courseGoals: string;
  courseDescription: string;
  creditScore: number;
  winter: boolean;
}

interface Comment {
  commentID: number;
  commentContent: string;
  commentDate: string;
  authorName: string;
}

const ThreadPage = () => {
  const { id } = useParams();
  const [thread, setThread] = useState<Course | undefined>();
  const [loading, setLoading] = useState<boolean>(false);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const userId = useAuthStore((s) => s.userId);
  const userDataStore = useUserDataStore();
  const [comments, setComments] = useState<Comment[]>([]);
  const [comment, setComment] = useState('');
  const token = localStorage.getItem('token');

  const fetchCourse = async () => {
    if (userId === null) return;

    try {
      setLoading(true);
      const response = await axiosInstance.get(`/courses/details/${id}`);
      setThread(response.data);
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

  useEffect(() => {
    const fetchData = async () => {
      if (userId !== null) {
        try {
          await fetchCourse();
          await fetchComments();
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

  return (
    <>
      {loading ? (
        <Spinner />
      ) : !thread ? (
        <div className="text-black text-center mt-6">
          <p>No enough data about your courses...</p>
          <p>First upload your certificate of passing exams!</p>
          <br />
        </div>
      ) : thread ? (
        <div className="container mx-auto">
          <div className="flex items-center justify-center mt-18 mb-14 text-black text-3xl">
            <p>{thread.courseName}</p>
          </div>
          <div className="flex gap-x-4">
            <div className="w-1/2">
              <p className="text-black mb-4 text-xl">Course Details</p>
              <Card>
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
                <CardContent className="border-b border-gray-400 flex items-center">
                  <span className="min-w-[100px]  pr-4">Goal</span>
                  <span className="pl-4 border-l border-gray-400">
                    {thread.courseGoals}
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
            <div className="w-1/2">
              <p className="text-black text-xl mb-4">
                 Opportunities after completing this course
              </p>
              <Card>
                <CardContent>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed
                  do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                  Ut enim ad minim veniam, quis nostrud exercitation ullamco
                  laboris nisi ut aliquip ex ea commodo consequat.
                </CardContent>
              </Card>
            </div>
          </div>
          <div className="flex gap-x-4">
            <div className="w-1/2">
              <p className="text-black my-4 text-xl">Comments</p>
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
              <p className="text-black my-4 text-xl">Cheat sheets</p>
              <Card>
                <CardContent>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed
                  do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                  Ut enim ad minim veniam, quis nostrud exercitation ullamco
                  laboris nisi ut aliquip ex ea commodo consequat.
                </CardContent>
              </Card>
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
