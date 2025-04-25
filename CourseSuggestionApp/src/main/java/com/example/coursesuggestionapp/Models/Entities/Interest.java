package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "interest")
public class Interest {
    @Id
    @Column(name = "interest_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @Column(name = "interest_name", nullable = false, length = 50)
    private String interestName;

    @Column(name = "interest_description", length = 150)
    private String interestDescription;

    @ManyToMany(mappedBy = "interests")
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Course> courses = new HashSet<>();
}
