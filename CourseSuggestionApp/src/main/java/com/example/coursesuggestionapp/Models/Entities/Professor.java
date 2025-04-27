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
@Table(name = "professor")
public class Professor {
    @Id
    @Column(name = "professor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long professorId;

    @Column(name = "professor_name", nullable = false, length = 250)
    private String professorName;

    @Column(name = "prof_email", nullable = true, length = 50)
    private String profEmail;

    @ManyToMany(mappedBy = "professors")
    private Set<Course> courses = new HashSet<>();

    public Professor() {

    }

    public Professor(String professorName, String profEmail) {
        this.professorName = professorName;
        this.profEmail = profEmail;
    }
}
