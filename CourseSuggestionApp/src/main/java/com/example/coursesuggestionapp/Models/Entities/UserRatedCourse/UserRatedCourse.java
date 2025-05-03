package com.example.coursesuggestionapp.Models.Entities.UserRatedCourse;

import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "user_rated_course")
public class UserRatedCourse {
    @Setter
    @EmbeddedId
    private UserRatedCourseId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "rating")
    private Integer rating;


    public UserRatedCourse() {

    }

    public UserRatedCourse(User user, Course course, Integer rating) {
        this.user = user;
        this.course = course;
        this.rating = rating;
    }

}
