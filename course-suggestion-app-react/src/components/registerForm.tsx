import React, { useState } from "react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuthStore } from "@/store/authStore.ts";
import { useNavigate } from "react-router";
import { AxiosError } from "axios";
import { FormError } from "./formError";

export function RegisterForm({ className, ...props }: React.ComponentProps<"div">) {
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [transcriptPdf, setTranscriptPdf] = useState<File | null>(null);
  const [formError, setFormError] = useState<string | null>(null);
  const [studyMajorId, setStudyMajorId] = useState(""); 

  const register = useAuthStore((s) => s.register);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      setFormError("Passwords do not match");
      return;
    }

    try {
      // Prepare FormData for multipart/form-data
      const formData = new FormData();
      formData.append("email", email);
      formData.append("first_name", firstName);
      formData.append("last_name", lastName);
      formData.append("password", password);
      formData.append("studyMajor", studyMajorId);

      if (transcriptPdf) {
        formData.append("transcriptPdf", transcriptPdf);
      }

      await register(formData); // Make sure your register action supports FormData

      navigate("/login");
    } catch (error: unknown) {
      if (error instanceof AxiosError) {
        setFormError(error.response?.data?.message || "Registration failed");
      } else {
        setFormError("Registration failed: Unknown error");
      }
      console.error(error);
    }
  };

  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card>
        <CardHeader>
          <CardTitle className="text-3xl text-center">REGISTER</CardTitle>
          <CardDescription className="text-center">Please enter your details.</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} encType="multipart/form-data">
            <div className="flex flex-col gap-6">
              {formError && <FormError message={formError} />}

              {/* Other inputs here ... */}
              <div className="grid gap-3">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="Enter your e-mail"
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                    setFormError(null);
                  }}
                  required
                />
              </div>

              <div className="grid gap-3">
                <Label htmlFor="firstName">First Name</Label>
                <Input
                  id="firstName"
                  type="text"
                  placeholder="Enter your name"
                  value={firstName}
                  onChange={(e) => {
                    setFirstName(e.target.value);
                    setFormError(null);
                  }}
                  required
                />
              </div>

              <div className="grid gap-3">
                <Label htmlFor="lastName">Last Name</Label>
                <Input
                  id="lastName"
                  type="text"
                  placeholder="Enter your surname"
                  value={lastName}
                  onChange={(e) => {
                    setLastName(e.target.value);
                    setFormError(null);
                  }}
                  required
                />
              </div>

              <div className="grid gap-3">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  placeholder="********"
                  onChange={(e) => {
                    setPassword(e.target.value);
                    setFormError(null);
                  }}
                  required
                />
              </div>

              <div className="grid gap-3">
                <Label htmlFor="confirmPassword">Repeat Password</Label>
                <Input
                  id="confirmPassword"
                  type="password"
                  value={confirmPassword}
                  placeholder="********"
                  onChange={(e) => {
                    setConfirmPassword(e.target.value);
                    setFormError(null);
                  }}
                  required
                />
              </div>

              <div className="grid gap-3">
  <Label htmlFor="studyMajor">Study Major</Label>
  <select
    id="studyMajor"
    value={studyMajorId}
    onChange={(e) => {
      setStudyMajorId(e.target.value);
      setFormError(null);
    }}
    required
    className="border rounded-md p-2"
  >
    <option value="">Select your major</option>
    <option value="1">Software engineering and information systems</option>
    <option value="2">Интернет, мрежи и безбедност</option>
    <option value="3">Информатичка едукација</option>
    <option value="4">Компјутерски Науки</option>
    <option value="5g">Компјутерско инженество</option>
    <option value="6">Примена на информациски технологии</option>
    <option value="7">Стручни студии за програмирање</option>
  </select>
</div>

              {/* PDF Upload */}
              <div className="grid gap-3">
  <Label htmlFor="transcriptPdf">Upload certificate (PDF)</Label>
  <input
    id="transcriptPdf"
    type="file"
    accept="application/pdf"
    className="hidden"
    onChange={(e) => {
      if (e.target.files && e.target.files.length > 0) {
        setTranscriptPdf(e.target.files[0]);
        setFormError(null);
      }
    }}
  />
  <label
    htmlFor="transcriptPdf"
    className="inline-block cursor-pointer rounded-md bg-[#9FCB8B] px-4 py-2 text-white text-center hover:bg-[#88b176] transition"
  >
    {transcriptPdf ? "Change Document" : "Certificate of passing exams"}
  </label>
</div>



              <div className="flex flex-col gap-3">
                <Button type="submit" className="w-full">
                  Register
                </Button>
              </div>
            </div>

            <div className="mt-4 text-center text-sm">
              Already have an account?{" "}
              <a href="/login" className="underline underline-offset-4">
                Login
              </a>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
