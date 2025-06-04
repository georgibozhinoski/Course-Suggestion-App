package com.example.coursesuggestionapp.Controller;


import com.example.coursesuggestionapp.Models.DTO.CheatSheetInfoDTO;
import com.example.coursesuggestionapp.Models.DTO.CheatSheetRequest;
import com.example.coursesuggestionapp.Models.Entities.CheatSheet;
import com.example.coursesuggestionapp.Models.Entities.File;
import com.example.coursesuggestionapp.Service.CheatSheetService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cheatsheets")
public class CheatSheetController {

    private final CheatSheetService cheatSheetService;

    public CheatSheetController(CheatSheetService cheatSheetService) {
        this.cheatSheetService = cheatSheetService;
    }

    @PostMapping
    public ResponseEntity<CheatSheet> uploadCheatSheet(@ModelAttribute CheatSheetRequest request) {
        return ResponseEntity.ok(cheatSheetService.createCheatSheet(request));
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<CheatSheetInfoDTO>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(cheatSheetService.getCheatSheetsByCourse(courseId));
    }

    @GetMapping("/file/{sheetId}")
    public ResponseEntity<byte[]> getFileById(@PathVariable Long sheetId) {
        try {
            byte[] contents = cheatSheetService.getFilesByCheatSheet(sheetId);
            return ResponseEntity.ok(contents);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CheatSheet>> getAll() {
        return ResponseEntity.ok(cheatSheetService.getAllCheatSheets());
    }

    @PutMapping("/{sheetId}/like")
    public ResponseEntity<?> likeCheatSheet(@PathVariable Long sheetId) {
        cheatSheetService.likeCheatSheet(sheetId);
        return ResponseEntity.ok("Cheat sheet liked successfully.");
    }
}