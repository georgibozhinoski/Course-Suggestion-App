package com.example.coursesuggestionapp.Models.Entities.UserCourse;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
public class UserCourseId implements Serializable {

    @Column(name = "id")
    private Long userId;

    @Column
    private Long courseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCourseId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {return Objects.hash(userId, courseId);}
}
