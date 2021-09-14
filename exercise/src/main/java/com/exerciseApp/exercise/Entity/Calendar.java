package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarRegister;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "calendar_id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String color;

    @Column(columnDefinition = "text")
    private String memo;


    // 생성자
    public Calendar(String start_date, String end_date, Member member) {
        this.startDate = LocalDate.parse(start_date);
        this.endDate = LocalDate.parse(end_date);
        this.member = member;
    }

    public Calendar(CalendarRegister calendarRegister, Member member) {
        this.startDate = LocalDate.parse(calendarRegister.getStartDate());
        this.endDate = LocalDate.parse(calendarRegister.getEndDate());
        this.memo = calendarRegister.getMemo();
        this.title = calendarRegister.getTitle();
        this.member = member;
        this.color = calendarRegister.getColor();
    }


}
