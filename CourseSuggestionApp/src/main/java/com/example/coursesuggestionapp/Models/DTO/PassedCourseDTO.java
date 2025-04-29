package com.example.coursesuggestionapp.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassedCourseDTO {

    private Long courseId;
    private String courseName;
    private String courseLevel;
    private String courseType;
    private String courseGrade;

    public PassedCourseDTO(Long courseId, String courseName, String courseLevel, String courseType, String courseGrade) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseType = courseType;
        this.courseGrade = courseGrade;
    }
}
