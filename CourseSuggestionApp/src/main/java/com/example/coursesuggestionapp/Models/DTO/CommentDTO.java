package com.example.coursesuggestionapp.Models.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CommentDTO {

    private Long commentID;
    private String commentContent;
    private LocalDateTime commentDate;
    private String authorName;

}
