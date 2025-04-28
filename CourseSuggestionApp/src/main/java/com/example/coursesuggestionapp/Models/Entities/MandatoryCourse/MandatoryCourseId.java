package com.example.coursesuggestionapp.Models.Entities.MandatoryCourse;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class MandatoryCourseId implements Serializable {

    private Long majorId;
    private Integer semesterNo;
    private Long courseId;

    public MandatoryCourseId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MandatoryCourseId)) return false;
        MandatoryCourseId that = (MandatoryCourseId) o;
        return Objects.equals(majorId, that.majorId) &&
                Objects.equals(semesterNo, that.semesterNo) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorId, semesterNo, courseId);
    }
}
