package com.example.coursesuggestionapp.Models.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CheatSheetRequest {
    private String sheetContent;
    private LocalDateTime sheetDate;
    private Long userId;
    private Long courseId;
    private List<MultipartFile> files;
}
