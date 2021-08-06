package com.exerciseApp.exercise.DTO.WorkoutDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class RoutineRegister {

    private String title;
    private String part;

    private List<VolumeRegister> volumes;
}
