package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.DTO.WorkoutDTO.VolumeRegister;
import lombok.*;
import org.hibernate.jdbc.Work;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Volume {

    @Id
    @GeneratedValue
    @Column(name = "volume_id")
    private Long id;

    private int num;
    private int sets;
    private int orders;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="routine_id")
    private Routine routine;

    public Volume(VolumeRegister vr) {
        this.num = vr.getNum();
        this.sets = vr.getSets();
        this.orders = vr.getOrders();
    }

    public Volume(int num, int sets, int orders, Workout workout, Routine routine) {
        this.num = num;
        this.sets = sets;
        this.orders = orders;
        this.workout = workout;
        this.routine = routine;
    }
}
