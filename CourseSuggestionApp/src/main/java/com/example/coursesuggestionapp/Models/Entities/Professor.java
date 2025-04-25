package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professor")
public class Professor {
    @Id
    @Column(name = "professor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long professorId;

    @Column(name = "professor_name", nullable = false, length = 50)
    private String professorName;

    @Column(name = "prof_email", nullable = false, length = 50)
    private String profEmail;

    @ManyToMany(mappedBy = "professors")
    private Set<Course> courses = new HashSet<>();
}
