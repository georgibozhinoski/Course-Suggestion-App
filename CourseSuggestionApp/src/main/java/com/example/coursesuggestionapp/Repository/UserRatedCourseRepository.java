package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.UserRatedCourse.UserRatedCourse;
import com.example.coursesuggestionapp.Models.Entities.UserRatedCourse.UserRatedCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRatedCourseRepository extends JpaRepository<UserRatedCourse, UserRatedCourseId> {
    Optional<UserRatedCourse> findByUser_IdAndCourse_CourseId(Long userId, Long courseId);

    @Query("SELECT AVG(urc.rating) FROM UserRatedCourse urc WHERE urc.course.courseId = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);
}
