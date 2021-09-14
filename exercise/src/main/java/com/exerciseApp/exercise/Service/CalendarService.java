package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarRegister;
import com.exerciseApp.exercise.DTO.CalendarDTO.ScheduleDTO;
import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Entity.Calendar;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    // 스케쥴 추가
    public ResData addSchedule(CalendarRegister cr, Member member) {
        Calendar calendar = new Calendar(cr, member);
        calendarRepository.save(calendar);
        return ResData.builder().message("스케쥴이 추가되었습니다").data(calendar.getId()).result_state(true).build();
    }

    // 메모변경
    public ResData changeMemo(Long calendar_id, String memo, Member member) {
        Optional<Calendar> findId = calendarRepository.findByIdAndMember(calendar_id, member);
        if (findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setMemo(memo);
        calendarRepository.save(calendar);
        return ResData.builder().message("메모가 추가 및 변경 되었습니다.").data(calendar.getId()).result_state(true).build();
    }

    // 메모변경
    public ResData changeSchedule(ScheduleDTO scheduleDTO, Member member) {
        Optional<Calendar> findId = calendarRepository.findByIdAndMember(scheduleDTO.getId(), member);
        if (findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setStartDate(LocalDate.parse(scheduleDTO.getStart()));
        calendar.setEndDate(LocalDate.parse(scheduleDTO.getEnd()));
        calendar.setMemo(scheduleDTO.getMemo());
        calendar.setTitle(scheduleDTO.getTitle());
        calendar.setColor(scheduleDTO.getColor());
        calendarRepository.save(calendar);
        return ResData.builder().message("해당 스케쥴이 변경되었습니다.").data(new ScheduleDTO(calendar)).result_state(true).build();
    }


    // 메모삭제
    public ResData deleteMemo(Long calendar_id, Member member) {
        Optional<Calendar> findId = calendarRepository.findByIdAndMember(calendar_id, member);
        if (findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setMemo("");
        calendarRepository.save(calendar);
        return ResData.builder().message("메모를 초기화 시켰습니다.").data(null).result_state(true).build();
    }

    // 스케쥴 삭제
    public ResData deleteSchedule(Long calendar_id, Member member) {
        Optional<Calendar> findId = calendarRepository.findByIdAndMember(calendar_id, member);
        if (findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendarRepository.delete(calendar);
        return ResData.builder().message("해당 스케쥴을 삭제했습니다").data(null).result_state(true).build();
    }

    // 날짜변경
    public void changeDate(Calendar calendar, LocalDate startDate, LocalDate endDate) {
        calendar.setStartDate(startDate);
        calendar.setEndDate(endDate);
        calendarRepository.save(calendar);
    }

    // 일정변경
    public ResData changeDate(Long calendar_id, LocalDate startDate, LocalDate endDate) {
        Optional<Calendar> findId = calendarRepository.findById(calendar_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setStartDate(startDate);
        calendar.setEndDate(endDate);
        calendarRepository.save(calendar);
        return ResData.builder().message("일정을 변경햇습니다.")
                .data(calendar.getId()).result_state(true).build();
    }


}
