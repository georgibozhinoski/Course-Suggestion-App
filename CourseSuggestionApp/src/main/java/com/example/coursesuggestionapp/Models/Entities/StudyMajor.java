package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@Entity
@Builder
@Table(name = "study_major")
public class StudyMajor {
    @Id
    @Column(name = "major_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long majorId;

    @Column(name = "major_name", nullable = false)
    private String majorName;

    @Column(name = "accreditation_year", nullable = false)
    private Integer accreditationYear;

    @OneToMany(mappedBy = "studyMajor")
    private List<User> users = new ArrayList<>();

    public StudyMajor() {

    }

    public StudyMajor(String majorName, Integer accreditationYear) {
        this.majorName = majorName;
        this.accreditationYear = accreditationYear;
    }
}
