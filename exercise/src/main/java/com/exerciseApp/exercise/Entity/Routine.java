package com.exerciseApp.exercise.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Routine {

    @Id
    @GeneratedValue
    @Column(name = "routine_id")
    private Long id;

    private String title;
    private String part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "routine",cascade = CascadeType.ALL)
    private List<Volume> volumes = new ArrayList<>();
}
