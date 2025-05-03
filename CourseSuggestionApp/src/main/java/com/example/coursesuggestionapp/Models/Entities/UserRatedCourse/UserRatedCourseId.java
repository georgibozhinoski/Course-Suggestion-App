package com.example.coursesuggestionapp.Models.Entities.UserRatedCourse;

import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourseId;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class UserRatedCourseId implements Serializable {
    @Column(name = "id")
    private Long userId;

    @Column
    private Long courseId;

    public UserRatedCourseId(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRatedCourseId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {return Objects.hash(userId, courseId);}
}
