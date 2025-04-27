package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Object> findByCourseCode(String courseCode);

    Optional<Course> findByCourseName(String courseName);
}
