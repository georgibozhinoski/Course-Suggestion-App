package com.example.coursesuggestionapp.Models.Entities.Semester;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SemesterId implements Serializable {

    @Column(name = "major_id")
    private Long studyMajorId;

    @Column(name = "semester_no")
    private Integer semesterNo;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof SemesterId)) return false;
        SemesterId that = (SemesterId) o;
        return semesterNo == that.semesterNo && Objects.equals(studyMajorId, that.studyMajorId);
    }

    @Override
    public int hashCode() { return Objects.hash(studyMajorId, semesterNo); }
}
