package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.DTO.WorkoutDTO.WorkoutRegister;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Workout extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "workout_id")
    private Long id;
    private String name;
    private String workout_img;
    private String part;
    private String target;
    private int e_type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Routine routine;

    public Workout(WorkoutRegister workoutRegister) {
        this.name = workoutRegister.getName();
        this.part = workoutRegister.getPart();
        this.target = workoutRegister.getTarget();
        this.e_type = workoutRegister.getE_type();
    }
}
