package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "recommendation_list")
public class RecommendationList {

    @Id
    @Column(name = "list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User sys_user;

    @ManyToMany
    @JoinTable(
            name = "listed_course",
            joinColumns = @JoinColumn(nullable = false, name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "list_id")
    )
    private Set<Course> courses = new HashSet<>();

    public RecommendationList() {

    }
    public RecommendationList(User sys_user) {
        this.sys_user = sys_user;
    }
}
