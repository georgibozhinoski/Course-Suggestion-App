package com.example.coursesuggestionapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.coursesuggestionapp.Models.Entities.MandatoryCourse.MandatoryCourse;

import java.util.List;

public interface MandatoryCourseRepository extends JpaRepository<MandatoryCourse, Long> {

    @Query("SELECT mc.courseId FROM MandatoryCourse mc")
    List<Long> findAllMandatoryCourseIds();

    List<MandatoryCourse> findAllByMajorId(Long majorId);
}
