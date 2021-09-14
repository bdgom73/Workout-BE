package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.DTO.WorkoutDTO.VolumeRegister;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Volume extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "volume_id")
    private Long id;

    private Integer num;
    private Integer sets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    public Volume(VolumeRegister vr) {
        this.num = vr.getNum();
        this.sets = vr.getSets();
    }

    public Volume(int num, int sets, Workout workout, Routine routine) {
        this.num = num;
        this.sets = sets;
        this.workout = workout;
        this.routine = routine;
    }

    public Volume(Volume volume, Routine routine) {
        this.num = volume.getNum();
        this.sets = volume.getSets();
        this.workout = volume.getWorkout();
        this.routine = routine;
        routine.getVolumes().add(this);
    }
}
