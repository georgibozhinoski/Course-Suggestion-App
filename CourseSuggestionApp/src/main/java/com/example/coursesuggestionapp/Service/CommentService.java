package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.Entities.Comment;

import java.util.List;


public interface CommentService {

    List<Comment> getCommentByCourseId(Long courseId);

    Comment saveComment(Comment comment);

}
