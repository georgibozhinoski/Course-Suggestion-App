package com.example.coursesuggestionapp.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CheatSheetInfoDTO {

    private String sheetName;
    private Long sheetId;
    private LocalDateTime sheetDate;
    private Integer sheetLikes;
    private Long userId;
    private Long courseId;
}

