export interface Course {
    courseId: number;
    courseCode: string;
    courseName: string;
    courseLevel: string;
    courseGoals: string;
    courseDescription: string;
    creditScore: number;
    winter: boolean;
    avgRating: number;
    userRating: number | undefined | null;
}

export interface Comment {
    commentID: number;
    commentContent: string;
    commentDate: string;
    authorName: string;
}

export interface CheatSheet {
    sheetName: string;
    sheetId: number;
    sheetDate: string;
    sheetLikes: number;
    userId: number;
    courseId: number;
}

export interface CreateCheatSheetDTO {
    sheetName: string;
    sheetContent: string;
    sheetDate: string;
    userId: number | undefined | null;
    courseId: number | undefined | null;
    file: string;
}