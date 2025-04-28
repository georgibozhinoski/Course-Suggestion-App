package com.example.coursesuggestionapp.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseDTO {

    private Long courseId;
    private String courseName;
    private String courseLevel;

    public CourseDTO(Long courseId, String courseName, String courseLevel) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseLevel = courseLevel;
    }
}
