package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    Optional<Course> findByCourseName(String courseName);

    @Query("""
        SELECT c
        FROM MandatoryCourse mc
        JOIN Course c ON mc.courseId = c.courseId
        WHERE mc.majorId = :majorId
        AND mc.semesterNo = :semesterNo
        """)
    List<Course> findCoursesByMajorIdAndSemesterNo(@Param("majorId") Long majorId, @Param("semesterNo") Integer semesterNo);

    @Query("""
        SELECT c
        FROM Course c
        WHERE c.courseId NOT IN (
            SELECT mc.courseId
            FROM MandatoryCourse mc
            WHERE mc.majorId = :majorId
        )
        AND c.courseLevel = :levelNo
        """)
    List<Course> findElectiveCoursesByMajorIdAndLevelNo(@Param("majorId") Long majorId, @Param("levelNo") Integer levelNo);

}
