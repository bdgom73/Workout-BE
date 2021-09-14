package com.exerciseApp.exercise.DTO.CalendarDTO;

import com.exerciseApp.exercise.DTO.MemberDTO.MemberDTO;
import com.exerciseApp.exercise.Entity.Calendar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CalendarDTO {

    private Long id;

    private LocalDate start;

    private LocalDate end;

    private String title;

    private String memo;

    private MemberDTO member;

    private String color;

    public CalendarDTO(Calendar calendar) {
        this.id = calendar.getId();
        this.start = calendar.getStartDate();
        this.end = calendar.getEndDate();
        this.memo = calendar.getMemo();
        this.title = calendar.getTitle();
        this.member = new MemberDTO(calendar.getMember());
        this.color = calendar.getColor();
    }

}
