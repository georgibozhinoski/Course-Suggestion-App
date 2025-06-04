import {Card, CardContent} from "@/components/ui/card";
import {useState} from "react";
import {Button} from "@/components/ui/button";
import axiosInstance from "@/api/axiosInstance";
import {Course} from "@/lib/interfaces.ts";
import {useAuthStore} from "@/store/authStore.ts";

interface RatingCardProps {
    course: Course;
    className?: string;
}

const RatingCard: React.FC<RatingCardProps> = ({course, className}) => {
    const [selectedRating, setSelectedRating] = useState<number | null>(null);
    const [submitted, setSubmitted] = useState<boolean>(false);
    const [submitting, setSubmitting] = useState<boolean>(false);

    const userId = useAuthStore((s) => s.userId);

    const handleRating = async () => {
        if (selectedRating == null || !userId || !course?.courseId) return;

        try {
            setSubmitting(true);
            await axiosInstance.post("/rating/course", {
                userId: userId,
                courseId: course.courseId,
                rating: selectedRating,
            });
            setSubmitted(true);
        } catch (error) {
            console.error("Error submitting rating:", error);
        } finally {
            setSubmitting(false);
        }
    };



    return (
        <Card className={"flex-1 w-full " + className}>
            <CardContent className="py-4 flex items-center justify-between gap-4 px-10">
                <div className={'inline w-1/3'}>
                    {course.avgRating === null ? (
                        <p>No ratings yet.</p>
                    ) : (
                        <div className="mb-2">
                            <p className="text-6xl font-bold">
                                {course.avgRating.toFixed(1)}
                                <span className="text-opacity-70 text-black text-xl font-normal"> / 5.0</span>
                            </p>
                            <p className="text-sm text-gray-500 mt-1">Average Score</p>
                        </div>

                    )}
                </div>

                <div className={'inline w-1/3'}>
                    {submitted ?
                        <p className="mt-2 text-green-600">Thanks for rating!</p>
                        : course.userRating != null ?
                            <p>You rated this course: {course.userRating} / 5</p>
                            :
                            <div className={'flex flex-col gap-2 mt-2'}>
                                <div className="flex space-x-2 my-2">
                                    {[1, 2, 3, 4, 5].map((value) => (
                                        <Button
                                            key={value}
                                            type="button"
                                            variant={selectedRating === value ? "default" : "outline"}
                                            onClick={() => setSelectedRating(value)}
                                        >
                                            {value}
                                        </Button>
                                    ))}
                                </div>
                                <Button onClick={handleRating} disabled={selectedRating === null || submitting}>
                                    {submitting ? "Submitting..." : "Submit Rating"}
                                </Button>
                            </div>
                    }
                </div>
            </CardContent>
        </Card>
    );
};

export default RatingCard;
