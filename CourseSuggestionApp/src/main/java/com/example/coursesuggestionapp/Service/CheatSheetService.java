package com.example.coursesuggestionapp.Service;

import com.example.coursesuggestionapp.Models.DTO.CheatSheetRequest;
import com.example.coursesuggestionapp.Models.Entities.CheatSheet;

import java.util.List;

public interface CheatSheetService {
    CheatSheet createCheatSheet(CheatSheetRequest request);
    List<CheatSheet> getCheatSheetsByCourse(Long courseId);
    List<CheatSheet> getAllCheatSheets();
    void likeCheatSheet(Long sheetId);


}