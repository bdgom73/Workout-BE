package com.exerciseApp.exercise.DTO.CalendarDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class CalendarRegister {

    private String date;
    private boolean isWorkout;
    private String memo;

}
