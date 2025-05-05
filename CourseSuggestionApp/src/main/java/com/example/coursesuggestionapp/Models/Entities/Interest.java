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

    @ManyToMany(mappedBy = "interests",fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Course> courses = new HashSet<>();

    public Interest() {

    }

    public Interest(String interestName) {
        this.interestName = interestName;
    }

    public Interest(String interestName, String interestDescription) {
        this.interestName = interestName;
        this.interestDescription = interestDescription;
    }
}
