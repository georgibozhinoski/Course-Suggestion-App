package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "cheat_sheet")
public class CheatSheet {
    @Id
    @Column(name = "sheet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sheetId;

    @Column(name = "sheet_content")
    private String sheetContent;

    @Column(name = "sheet_date")
    private LocalDateTime sheetDate;

    @Column(name = "sheet_likes")
    private Integer sheetLikes;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "sheet")
    private List<File> files;

    public CheatSheet() {

    }

    public CheatSheet(String sheetContent, LocalDateTime sheetDate, Integer sheetLikes, User user, Course course) {
        this.sheetContent = sheetContent;
        this.sheetDate = sheetDate;
        this.sheetLikes = sheetLikes;
        this.user = user;
        this.course = course;
    }
}
