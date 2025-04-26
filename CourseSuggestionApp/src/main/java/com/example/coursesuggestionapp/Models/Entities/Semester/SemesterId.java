package com.example.coursesuggestionapp.Models.Entities.Semester;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class SemesterId implements Serializable {

    @Column(name = "major_id")
    private Long studyMajorId;

    @Column(name = "semester_no")
    private Integer semesterNo;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof SemesterId that)) return false;
        return Objects.equals(semesterNo, that.semesterNo) && Objects.equals(studyMajorId, that.studyMajorId);
    }

    @Override
    public int hashCode() { return Objects.hash(studyMajorId, semesterNo); }
}
