package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recommendation_list")
public class RecommendationList {

    @Id
    @Column(name = "list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;

    @ManyToMany
    @JoinTable(
            name = "listed_course",
            joinColumns = @JoinColumn(nullable = false, name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "list_id")
    )
    private Set<Course> courses = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User sys_user;
}
