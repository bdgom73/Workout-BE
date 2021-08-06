package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.MemberDTO.Register;
import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CreateIDService createIDService;
    @Test
    void memberTotalServiceTest(){

    }
}