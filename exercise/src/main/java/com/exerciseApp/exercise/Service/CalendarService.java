package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarDTO;
import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarRegister;
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
    public ResData addSchedule(CalendarRegister cr, Member member){
        Calendar calendar = new Calendar(cr,member);
        Calendar c = calendarRepository.save(calendar);
        return ResData.builder().message("스케쥴이 추가되었습니다").data(new CalendarDTO(c)).result_state(true).build();
    }

    // 메모변경
    public ResData changeMemo(Long calendar_id, String memo){
        Optional<Calendar> findId = calendarRepository.findById(calendar_id);
        if(findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setMemo(memo);
        calendarRepository.save(calendar);
        return ResData.builder().message("메모가 추가 및 변경 되었습니다.").data(calendar.getId()).result_state(true).build();
    }

    // 운동여부 상태변경
    public ResData changeWorkoutStatus(Long calendar_id, boolean isWorkout){
        Optional<Calendar> findId = calendarRepository.findById(calendar_id);
        if(findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setWorkout(isWorkout);
        calendarRepository.save(calendar);
        return ResData.builder().message("운동여부가 "+isWorkout+"로 변경되었습니다")
                .data(calendar.getId()).result_state(true).build();
    }

    // 메모삭제
    public ResData deleteMemo(Long calendar_id){
        Optional<Calendar> findId = calendarRepository.findById(calendar_id);
        if(findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setMemo("");
        calendarRepository.save(calendar);
        return ResData.builder().message("메모를 초기화 시켰습니다.").data(null).result_state(true).build();
    }

    // 날짜변경
    public void changeDate(Calendar calendar, LocalDate date){
        calendar.setCurrentDate(date);
        calendarRepository.save(calendar);
    }
    public ResData changeDate(Long calendar_id, LocalDate date){
        Optional<Calendar> findId = calendarRepository.findById(calendar_id);
        if(findId.isEmpty()) return ResData.builder().message("존재하지않는 일정입니다.").data(null).result_state(false).build();
        Calendar calendar = findId.get();
        calendar.setCurrentDate(date);
        calendarRepository.save(calendar);
        return ResData.builder().message(calendar.getCurrentDate()+"에서 "+date + " 로 변경햇습니다.")
                .data(calendar.getId()).result_state(true).build();
    }
}
