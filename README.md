![fcse_logo](https://github.com/BeratAhmetaj/Museudonia/blob/main/Gif%20Animations/Logo_FINKI_UKIM_EN/Logo_FINKI_UKIM_EN_00000.png)

#  FINKI Course Recommendation App

A smart course recommendation platform for students at **FINKI**, designed to help them choose the best elective courses based on their academic history, preferences, and goals.
![Gif](https://github.com/georgibozhinoski/Course-Suggestion-App/tree/main/CourseSuggestionApp/Readme/first.gif)

## 🚀 Features

###  Student Profile
- JWT-based sign up & login
- Upload passed-exams certificate
- Pick profile pictures via Unsplash API
- Edit profile & change password

###  Smart Course Recommendations
- AI suggests electives based on:
  - Passed exams, grades, semester, and level (L1–L3)
  - Summer or winter semester preference
  - Optional custom prompt input
- Python engine calculates course similarity & success likelihood
  
###  Course Explorer
- View detailed course info (description, goals, prerequisites)
- Leave 1–5 star reviews and comments
- Upload/download student-shared cheat sheets

![Gif](https://github.com/georgibozhinoski/Course-Suggestion-App/tree/main/CourseSuggestionApp/Readme/second.gif)

## 🛠 Tech Stack

###  Backend
- **Java Spring Boot**
- **JWT Authentication**
- **Azure-hosted SQL Database**
- **Data Initializers** - Auto-loads professors, courses, semesters & prerequisites from scraped FINKI data on DB start

![DatabaseImage](https://github.com/georgibozhinoski/Course-Suggestion-App/blob/main/CourseSuggestionApp/Readme/imagedb.png)

###  Frontend
- **React + Vite**
- **ShadCN UI Components**
- **REST API integration**
- Responsive, modern UI/UX

###  Recommendation Engine
- **Python script** analyzing:
  - Grade performance
  - Completed courses
  - Student level & semester
  - Similarity metrics for suggesting relevant electives

We have a full UI/UX flow for the platform in Figma, covering:
👉 [Figma Link](https://www.figma.com/design/bsp6k7knpCKj8OJg7PuU4f/Untitled--Copy-?node-id=0-1&t=uE5Sfh89S4BPHjfy-1)


