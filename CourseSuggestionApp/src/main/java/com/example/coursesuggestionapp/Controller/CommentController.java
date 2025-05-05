package com.example.coursesuggestionapp.Controller;


import com.example.coursesuggestionapp.Models.DTO.AddCommentDTO;
import com.example.coursesuggestionapp.Models.DTO.CommentDTO;
import com.example.coursesuggestionapp.Models.Entities.Comment;
import com.example.coursesuggestionapp.Models.Entities.Course;
import com.example.coursesuggestionapp.Models.Entities.User;
import com.example.coursesuggestionapp.Repository.CourseRepository;
import com.example.coursesuggestionapp.Service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    private final CourseRepository courseRepository;


    public CommentController(CommentService commentService, CourseRepository courseRepository) {
        this.commentService = commentService;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/course/{courseId}")
    public List<CommentDTO> getCommentsByCourseId(@PathVariable Long courseId) {
//        return commentService.getCommentByCourseId(courseId);
        List<Comment> comments = commentService.getCommentByCourseId(courseId);

        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/course/{courseId}")
    public Comment addComment (@PathVariable Long courseId,
                               @RequestBody AddCommentDTO addCommentDTO,
                               @AuthenticationPrincipal User user) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Comment comment = new Comment();
        comment.setCommentDate(LocalDateTime.now());
        comment.setCommentContent(addCommentDTO.getCommentContent());
        comment.setAuthor(user);
        comment.setCourse(course);

        return commentService.saveComment(comment);
    }



    private CommentDTO convertToDTO(Comment comment) {
        String authorName = comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName();
        return new CommentDTO(
                comment.getCommentId(),
                comment.getCommentContent(),
                comment.getCommentDate(),
                authorName
        );
    }


}
