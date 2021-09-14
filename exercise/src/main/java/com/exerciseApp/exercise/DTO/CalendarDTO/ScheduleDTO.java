package com.exerciseApp.exercise.DTO.CalendarDTO;

import com.exerciseApp.exercise.Entity.Calendar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDTO {

    private Long id;
    private String title;
    private String start;
    private String end;
    private String memo;
    private String color;

    public ScheduleDTO(Calendar calendar) {
        this.id = calendar.getId();
        this.title = calendar.getTitle();
        this.start = calendar.getStartDate().toString();
        this.end = calendar.getEndDate().toString();
        this.memo = calendar.getMemo();
        this.color = calendar.getColor();
    }
}
