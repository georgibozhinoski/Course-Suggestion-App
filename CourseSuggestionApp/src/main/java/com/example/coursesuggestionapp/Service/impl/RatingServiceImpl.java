package com.example.coursesuggestionapp.Service.impl;

import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Models.Entities.UserRatedCourse.UserRatedCourse;
import com.example.coursesuggestionapp.Models.Entities.UserRatedCourse.UserRatedCourseId;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Repository.UserRatedCourseRepository;
import com.example.coursesuggestionapp.Repository.UserRepository;
import com.example.coursesuggestionapp.Service.RatingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    private final UserRatedCourseRepository repository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public RatingServiceImpl(UserRatedCourseRepository repository,
                             UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void rateCourse(Long userId, Long courseId, Integer rating) {
        Optional<UserRatedCourse> existingRating = repository.findByUser_IdAndCourse_CourseId(userId, courseId);

        if (existingRating.isPresent()) {
            existingRating.get().setRating(rating);
            repository.save(existingRating.get());
        }
        else{
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            UserRatedCourseId id = new UserRatedCourseId(userId, courseId);
            UserRatedCourse userRatedCourse = new UserRatedCourse(user, course, rating);
            userRatedCourse.setId(id);
            repository.save(userRatedCourse);
        }
    }

    @Override
    public Double getCourseRating(Long courseId){
        return repository.findAverageRatingByCourseId(courseId);
    }

    @Override
    public Integer getUserRatingForCourse(Long courseId, Long userId){
        Optional<UserRatedCourse> userRatedCourse = repository.findByUser_IdAndCourse_CourseId(userId, courseId);
        return userRatedCourse.map(UserRatedCourse::getRating).orElse(null);
    }
}
