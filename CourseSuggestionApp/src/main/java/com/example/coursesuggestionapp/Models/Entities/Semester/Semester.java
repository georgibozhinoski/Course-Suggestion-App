package com.example.coursesuggestionapp.Models.Entities.Semester;

import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.StudyMajor;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "semester")
public class Semester {

    @EmbeddedId
    private SemesterId semesterId;

    @MapsId("studyMajorId")
    @ManyToOne
    @JoinColumn(name = "major_id")
    private StudyMajor studyMajor;

    @Column(name = "is_winter")
    private Boolean isWinter;

    @Column(name = "level_of_elective_courses")
    private String levelOfElectiveCourses;

    @Column(name = "num_elective_courses")
    private Integer numElectiveCourses;

    @ManyToMany
    @JoinTable(
            name = "mandatory_course",
            joinColumns = {
                    @JoinColumn(name = "major_id", referencedColumnName = "major_id"),
                    @JoinColumn(name = "semester_no", referencedColumnName = "semester_no")
            },
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

}
