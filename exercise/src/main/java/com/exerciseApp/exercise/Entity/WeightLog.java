package com.exerciseApp.exercise.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeightLog {

    @Id
    @GeneratedValue
    @Column(name = "weight_log_id")
    private Long id;

    private LocalDateTime date;
    private Double weight;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public WeightLog(Double weight, Member member) {
        this.weight = weight;
        this.date = LocalDateTime.now();
        this.member = member;
    }
}
