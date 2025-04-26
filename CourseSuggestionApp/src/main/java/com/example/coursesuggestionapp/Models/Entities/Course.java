package com.example.coursesuggestionapp.Models.Entities;

import com.example.coursesuggestionapp.Models.Entities.Semester.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(name = "course_name", nullable = false, length = 250)
    private String courseName;

    @Column(name = "course_level", nullable = false, length = 10)
    private String courseLevel;

    @Column(name = "is_winter", nullable = false)
    private boolean isWinter;

    @Column(name = "course_code", nullable = false, length = 10, unique = true)
    private String courseCode;

    @Column(name = "course_goals", nullable = false, length = 3500)
    private String courseGoals;

    @Column(name = "course_description", nullable = false, length = 3500)
    private String courseDescription;

    @Column(name = "credit_score", nullable = false)
    private Short creditScore;

    @Column(name = "prerequisite_credits",length = 300)
    private String prerequisiteCredits;

    @OneToMany(mappedBy = "course")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<CheatSheet> sheets = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Semester> semesters = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<RecommendationList> recommendationLists = new HashSet<>();



    @ManyToMany
    @JoinTable(
            name = "course_professor",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private Set<Professor> professors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "course_related_interest",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> courses = new HashSet<>();



    public Course() {

    }

    public Course(String courseName, String courseLevel, boolean isWinter, String courseCode, String courseGoals, String courseDescription, Short creditScore) {
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.isWinter = isWinter;
        this.courseCode = courseCode;
        this.courseGoals = courseGoals;
        this.courseDescription = courseDescription;
        this.creditScore = creditScore;
    }
}

