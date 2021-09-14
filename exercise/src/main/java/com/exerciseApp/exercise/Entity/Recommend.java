package com.exerciseApp.exercise.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "recommend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Boolean isRecommend;
}
