package com.example.coursesuggestionapp.Models.Entities.UserCourse;

import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "user_passed_course")
public class UserCourse {

    @EmbeddedId
    private UserCourseId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "session")
    private String session;

    public UserCourse() {

    }

    public UserCourse(User user, Course course, Integer grade, String session) {
        this.user = user;
        this.course = course;
        this.grade = grade;
        this.session = session;
    }
}
