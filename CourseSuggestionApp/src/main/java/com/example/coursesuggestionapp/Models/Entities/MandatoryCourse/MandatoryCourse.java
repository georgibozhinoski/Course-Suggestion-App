package com.example.coursesuggestionapp.Models.Entities.MandatoryCourse;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "mandatory_course")
@IdClass(MandatoryCourseId.class)
public class MandatoryCourse {

    @Id
    @Column(name = "major_id")
    private Long majorId;

    @Id
    @Column(name = "semester_no")
    private Integer semesterNo;

    @Id
    @Column(name = "course_id")
    private Long courseId;

}
