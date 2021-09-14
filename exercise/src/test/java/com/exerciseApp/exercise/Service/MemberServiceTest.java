package com.exerciseApp.exercise.Service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberServiceTest {


    @Test
    void memberTotalServiceTest() {

    }
}