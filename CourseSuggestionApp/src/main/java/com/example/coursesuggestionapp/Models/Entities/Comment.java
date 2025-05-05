package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToMany
    @JoinTable(
            name = "comment_liked_by",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<User> likedByUsers = new HashSet<>();

    public Comment() {

    }

    public Comment(LocalDateTime commentDate, String commentContent, User author, Course course) {
        this.commentDate = commentDate;
        this.commentContent = commentContent;
        this.author = author;
        this.course = course;
    }
}
