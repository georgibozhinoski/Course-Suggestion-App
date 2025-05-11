package com.example.coursesuggestionapp.Controller;

import com.example.coursesuggestionapp.Models.DTO.RateCourseRequest;
import com.example.coursesuggestionapp.Service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/course")
    public void rateCourse(@RequestBody RateCourseRequest request) {
        ratingService.rateCourse(request.getUserId(), request.getCourseId(), request.getRating());
    }

    @GetMapping("/course/{courseId}/average")
    public Double getAverageRating(@PathVariable Long courseId) {
        return ratingService.getCourseRating(courseId);
    }

    @GetMapping("/course/{courseId}/user/{userId}")
    public Integer getUserRating(
            @PathVariable Long courseId,
            @PathVariable Long userId
    ) {
        return ratingService.getUserRatingForCourse(courseId, userId);
    }
}
