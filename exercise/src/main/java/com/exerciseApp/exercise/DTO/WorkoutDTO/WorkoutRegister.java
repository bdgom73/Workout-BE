package com.exerciseApp.exercise.DTO.WorkoutDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class WorkoutRegister {

    private String name;

    private String part;
    private Integer e_type;

    private String explanation;

}
