package com.example.demo.src.file.domain;

import com.example.demo.src.file.WorkTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name ="Works")
@EqualsAndHashCode(callSuper = false)
public class Works extends WorkTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Teams teams;

    @Column(name = "work_name", length = 50)
    private String workName;



    @Column(name = "importance")
    private Integer importance;

    @Column(name = "status")
    private Integer status;

    @Column(name = "worker_number")
    private Integer workerNumber;

    @Column(name = "del_yn")
    private boolean delYn;

    @OneToMany(mappedBy = "works", cascade = ALL, orphanRemoval = true)
    private List<Boards> boardsList = new ArrayList<>();

    @OneToMany(mappedBy = "works", cascade = ALL, orphanRemoval = true)
    private List<Workers> workersList = new ArrayList<>();

    //피드백 추가,연관관계 편의 메소드
    public void addBoards(Boards boards){
        //comment의 Post 설정은 comment에서 함
        boardsList.add(boards);
    }
}
