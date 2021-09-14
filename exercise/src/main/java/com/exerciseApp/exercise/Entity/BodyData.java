package com.exerciseApp.exercise.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BodyData {

    @Id
    @GeneratedValue
    @Column(name = "body_data_id")
    private Long id;

    private Integer age;

    private Double height;
    private Double weight;

    // 골격근량
    private Double SMM;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
