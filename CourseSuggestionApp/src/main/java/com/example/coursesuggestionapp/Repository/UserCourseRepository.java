package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourse;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {
    List<UserCourse> findAllByUser(User user);
}

