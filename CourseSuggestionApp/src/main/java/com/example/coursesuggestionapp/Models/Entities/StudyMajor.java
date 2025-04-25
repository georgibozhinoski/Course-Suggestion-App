package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_major")
public class StudyMajor {
    @Id
    @Column(name = "major_id")
    @GeneratedValue
    private Long majorId;

    @Column(name = "major_name")
    private String majorName;

    @Column(name = "acreditation_year")
    private Integer acreditationYear;

    @OneToMany(mappedBy = "studyMajor")
    private List<User> users = new ArrayList<>();
}
