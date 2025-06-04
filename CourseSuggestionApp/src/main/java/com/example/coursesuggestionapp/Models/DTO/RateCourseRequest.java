package com.example.coursesuggestionapp.Models.DTO;

import lombok.Data;

@Data
public class RateCourseRequest {
    Long userId;
    Long courseId;
    Integer rating;
}
