package com.example.coursesuggestionapp.Models.DTO;

import com.example.coursesuggestionapp.Models.Entities.Course;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseInfoDTO {
    private Long courseId;
    private String courseCode;
    private String courseDescription;
    private String courseGoals;
    private String courseLevel;
    private String courseName;
    private Short creditScore;
    private boolean isWinter;
    private String prerequisiteCredits;
    private Double avgRating;
    private Integer userRating;

    public CourseInfoDTO(Course course, Double avgRating, Integer userRating) {
        this.courseId = course.getCourseId();
        this.courseCode = course.getCourseCode();
        this.courseDescription = course.getCourseDescription();
        this.courseGoals = course.getCourseGoals();
        this.courseLevel = course.getCourseLevel();
        this.courseName = course.getCourseName();
        this.creditScore = course.getCreditScore();
        this.isWinter = course.isWinter();
        this.prerequisiteCredits = course.getPrerequisiteCredits();
        this.avgRating = avgRating;
        this.userRating = userRating;
    }
}
