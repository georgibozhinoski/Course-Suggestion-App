package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    @Column(name = "comment_content")
    private String commentContent;

    @ManyToMany
    @JoinTable(
            name = "comment_liked_by",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<User> likedByUsers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
