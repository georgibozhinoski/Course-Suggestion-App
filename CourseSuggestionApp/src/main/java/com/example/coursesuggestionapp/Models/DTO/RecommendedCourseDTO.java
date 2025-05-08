package com.example.coursesuggestionapp.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendedCourseDTO {
    private CourseDTO course;
    private String extraData;
}
