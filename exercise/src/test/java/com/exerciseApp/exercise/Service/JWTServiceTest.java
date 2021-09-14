package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.Repository.CalendarRepository;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Repository.RecommendRepository;
import com.exerciseApp.exercise.Repository.RoutineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Locale;

@SpringBootTest
@Transactional
@Rollback(false)
class JWTServiceTest {

    @Autowired
    JWTService jwtService;
    @Autowired
    CreateIDService createIDService;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    RecommendRepository recommendRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoutineRepository routineRepository;


    @Test
    public void test() {
        String str = "aBcDefGhij";
        String s = str.toUpperCase(Locale.ROOT);
        System.out.println(s);
    }
}