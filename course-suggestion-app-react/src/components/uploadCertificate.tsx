import { FormError } from "@/components/formError";
import { Progress } from "@/components/ui/progress";
import { useUserDataStore, UserInfo } from "@/store/userDataStore";
import { useEffect, useState } from "react";
import { AxiosError } from "axios";
const majors = [
  { text: "Software engineering and information systems", value: "1" },
  { text: "Интернет, мрежи и безбедност", value: "2" },
  { text: "Информатичка едукација", value: "3" },
  { text: "Компјутерски Науки", value: "4" },
  { text: "Компјутерско инженество", value: "5" },
  { text: "Примена на информациски технологии", value: "6" },
  { text: "Стручни студии за програмирање", value: "7" },
];

interface UpdateUserDataFormProps {
  user: UserInfo;
  disabled?: boolean;
  onSubmit: () => void;
}

export default function UploadCertificate({
  user,
  disabled = false,
  onSubmit,
}: UpdateUserDataFormProps) {
  const [formError, setFormError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [progressValue, setProgressValue] = useState(0);
  const updateUser = useUserDataStore((state) => state.updateUserInfo);
  const [formData, setFormData] = useState({
    email: user.email,
    firstName: user.firstName,
    lastName: user.lastName,
    studyMajorId: "",
  });

  useEffect(() => {
    const major = majors.find((m) => m.text === user.studyMajor);
    setFormData((prev) => ({ ...prev, studyMajorId: major?.value || "" }));
  }, [user]);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setFormError(null);
    setLoading(true);
    setProgressValue(10);

    const interval = setInterval(() => {
      setProgressValue((prev) => (prev >= 90 ? prev : prev + 10));
    }, 500);

    try {
      await updateUser(
        {
          id: user.id,
          newFirstName: formData.firstName,
          newLastName: formData.lastName,
          newStudyMajorId: Number(formData.studyMajorId),
        },
        file
      );

      clearInterval(interval);
      setProgressValue(100);
      onSubmit();
      window.location.reload();
    } catch (error) {
      clearInterval(interval);
      setProgressValue(0);

      const message =
        error instanceof AxiosError
          ? error.response?.data?.message || "Update failed"
          : "Update failed: Unknown error";

      setFormError(message);
      console.error("Update failed:", error);
      window.location.reload();

    } finally {
      setLoading(false);
      window.location.reload();

    }
  };

  return (
    <div className="flex flex-col gap-6">
      {formError && <FormError message={formError} />}
      {!disabled && (
        <>
          <div className="grid gap-3">
            <input
              id="transcriptPdf"
              type="file"
              accept="application/pdf"
              className="hidden"
              onChange={handleFileChange}
            />
            <label
              htmlFor="transcriptPdf"
              className="inline-block cursor-pointer rounded-md bg-[#9FCB8B] px-4 py-2 text-white text-center hover:bg-[#88b176] transition"
            >
              Upload Certificate (PDF)
            </label>
          </div>

          {loading && <Progress className="mt-1" value={progressValue} />}
        </>
      )}
    </div>
  );
}
