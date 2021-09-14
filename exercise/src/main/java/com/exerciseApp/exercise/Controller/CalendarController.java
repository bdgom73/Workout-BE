package com.exerciseApp.exercise.Controller;

import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarDTO;
import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarRegister;
import com.exerciseApp.exercise.DTO.CalendarDTO.ScheduleDTO;
import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Entity.Calendar;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Repository.CalendarRepository;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Service.CalendarService;
import com.exerciseApp.exercise.Service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/myApi/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarRepository calendarRepository;
    private final JWTService jwtService;
    private final MemberRepository memberRepository;
    private final CalendarService calendarService;
    private final String AUTH = "Authorization";

    @GetMapping("/get/all/{start}/{end}")
    public ResData getAllCalenderList(
            @RequestHeader(AUTH) String token,
            @PathVariable("start") String start,
            @PathVariable("end") String end
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        List<Calendar> calendarList = calendarRepository.findByMemberAndStartDateBetween(member, LocalDate.parse(start), LocalDate.parse(end));
        List<CalendarDTO> resultList = calendarList.stream().map(CalendarDTO::new).collect(Collectors.toList());
        return ResData.builder().message(start + " ~ " + end + " 범위의 데이터를 가져왔습니다")
                .data(resultList).result_state(false).build();
    }

    @PostMapping("/add/schedule")
    public ResData addSchedule(
            @RequestHeader(AUTH) String token,
            @RequestBody CalendarRegister calendarRegister
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        return calendarService.addSchedule(calendarRegister, member);
    }

    @PostMapping("/change/schedule")
    public ResData changeSchedule(
            @RequestHeader(AUTH) String token,
            @RequestBody ScheduleDTO scheduleDTO
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        return calendarService.changeSchedule(scheduleDTO, member);
    }

    @DeleteMapping("/delete/schedule/{calendar_id}")
    public ResData changeSchedule(
            @RequestHeader(AUTH) String token,
            @PathVariable("calendar_id") String calendar_id
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        return calendarService.deleteSchedule(Long.parseLong(calendar_id), member);
    }
}
