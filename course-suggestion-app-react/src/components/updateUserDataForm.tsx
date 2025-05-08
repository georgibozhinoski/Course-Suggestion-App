import { FormError } from "@/components/formError";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Progress } from "@/components/ui/progress";
import { useUserDataStore, UserInfo } from "@/store/userDataStore";
import { useNavigate } from "react-router";
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

export default function UpdateUserDataForm({
  user,
  disabled = false,
  onSubmit,
}: UpdateUserDataFormProps) {
  const [formData, setFormData] = useState({
    email: user.email,
    firstName: user.firstName,
    lastName: user.lastName,
    studyMajorId: "",
  });

  const [transcriptPdf, setTranscriptPdf] = useState<File | null>(null);
  const [formError, setFormError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [progressValue, setProgressValue] = useState(0);

  const updateUser = useUserDataStore((state) => state.updateUserInfo);
  const navigate = useNavigate();

  useEffect(() => {
    const major = majors.find((m) => m.text === user.studyMajor);
    setFormData((prev) => ({ ...prev, studyMajorId: major?.value || "" }));
  }, [user]);

  const handleChange = (field: keyof typeof formData) => (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [field]: e.target.value });
    setFormError(null);
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    setTranscriptPdf(file);
    setFormError(null);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
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
        transcriptPdf ?? undefined 
      );

      clearInterval(interval);
      setProgressValue(100);

      onSubmit();
      navigate("/profile");
    } catch (error) {
      clearInterval(interval);
      setProgressValue(0);

      const message =
        error instanceof AxiosError
          ? error.response?.data?.message || "Update failed"
          : "Update failed: Unknown error";

      setFormError(message);
      console.error("Update failed:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-6">
      {formError && <FormError message={formError} />}

      <div className="grid gap-3">
        <Label htmlFor="email">Email</Label>
        <Input id="email" type="email" value={formData.email} disabled />
      </div>

      <div className="grid gap-3">
        <Label htmlFor="firstName">First Name</Label>
        <Input
          id="firstName"
          type="text"
          value={formData.firstName}
          onChange={handleChange("firstName")}
          disabled={disabled}
          required
        />
      </div>

      <div className="grid gap-3">
        <Label htmlFor="lastName">Last Name</Label>
        <Input
          id="lastName"
          type="text"
          value={formData.lastName}
          onChange={handleChange("lastName")}
          disabled={disabled}
          required
        />
      </div>

      <div className="grid gap-3">
        <Label htmlFor="studyMajor">Study Major</Label>
        <select
          id="studyMajor"
          value={formData.studyMajorId}
          onChange={handleChange("studyMajorId")}
          disabled={disabled}
          required
          className="border rounded-md p-2 dark:text-white dark:bg-gray-800 dark:border-gray-600" 
        >
          <option value="">Select your major</option>
          {majors.map((major) => (
            <option key={major.value} value={major.value}>
              {major.text}
            </option>
          ))}
        </select>
      </div>

      {!disabled && (
        <>
          <div className="grid gap-3">
            <Label htmlFor="transcriptPdf">Upload certificate (PDF)</Label>
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
              {transcriptPdf ? "Change Document" : "Certificate of passing exams"}
            </label>
          </div>

          <div className="flex flex-col gap-3">
            <Button type="submit" className="w-full" disabled={loading}>
              Update Profile
            </Button>
            {loading && <Progress className="mt-1" value={progressValue} />}
          </div>
        </>
      )}
    </form>
  );
}
