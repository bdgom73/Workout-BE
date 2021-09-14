package com.exerciseApp.exercise.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Routine extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "routine_id")
    private Long id;

    private String title;
    private String part;

    private Boolean share;

    @Column(name = "original_author")
    private Long originalAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<Volume> volumes = new ArrayList<>();

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    @Transient
    private Integer score;

    public Routine(Routine routine, Member member) {
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.share = Boolean.FALSE;
        this.originalAuthor = routine.member.getId();
        this.member = member;
    }
}
