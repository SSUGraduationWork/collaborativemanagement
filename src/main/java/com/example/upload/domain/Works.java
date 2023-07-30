package com.example.upload.domain;

import com.example.upload.WorkTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="Works")
@Data
@EqualsAndHashCode(callSuper = false)
public class Works extends WorkTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Teams team;

    @Column(name = "work_name", length = 50)
    private String workName;



    @Column(name = "importance")
    private Integer importance;

    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status;

    @Column(name = "worker_number")
    private Integer workerNumber;

    @Column(name = "delete", nullable = false, columnDefinition = "tinyint(1) default 0")
    private boolean delete;

    @OneToMany(mappedBy = "works", cascade = ALL, orphanRemoval = true)
    private List<Boards> boardList = new ArrayList<>();

    //피드백 추가,연관관계 편의 메소드
    public void addBoards(Boards boards){
        //comment의 Post 설정은 comment에서 함
        boardList.add(boards);
    }
}
