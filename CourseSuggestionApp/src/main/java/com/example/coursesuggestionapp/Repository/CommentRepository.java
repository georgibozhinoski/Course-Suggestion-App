package com.example.coursesuggestionapp.Repository;

import com.example.coursesuggestionapp.Models.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
