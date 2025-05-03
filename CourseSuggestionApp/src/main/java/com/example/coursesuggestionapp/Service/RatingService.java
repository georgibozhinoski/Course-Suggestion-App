package com.example.coursesuggestionapp.Service;

public interface RatingService {
    void rateCourse(Long userId, Long courseId, Integer rating);
    Double getCourseRating(Long courseId);
}
