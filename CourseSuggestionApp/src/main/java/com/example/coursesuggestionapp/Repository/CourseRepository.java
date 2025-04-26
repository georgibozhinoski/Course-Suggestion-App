package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
