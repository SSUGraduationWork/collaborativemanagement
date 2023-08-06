package com.example.upload.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(length = 100)
    private String filename;

    @Column(length = 100)
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Boards boards;

    //연관관계 편의 메소드
    public void confirmBoard(Boards boards){
        this.boards=boards;
        boards.addFiles(this);
    }

    @Builder
    public Files(String filename, String filepath){
        this.filename=filename;
        this.filepath=filepath;
    }
}