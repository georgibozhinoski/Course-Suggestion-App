package com.example.coursesuggestionapp.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "file")
public class File {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(name = "file_name", nullable = false, length = 50)
    private String fileName;

    @Column(name = "file_content", nullable = false)
    @Lob
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "sheet_id", nullable = false)
    private CheatSheet sheet;

    public File() {

    }

    public File(String fileName, byte[] content, CheatSheet sheet) {
        this.fileName = fileName;
        this.content = content;
        this.sheet = sheet;
    }
}
