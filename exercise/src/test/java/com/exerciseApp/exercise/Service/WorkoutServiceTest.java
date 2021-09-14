package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.Entity.DoWorkout;
import com.exerciseApp.exercise.Repository.DoWorkoutRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class WorkoutServiceTest {


    @Autowired
    DoWorkoutRepository doWorkoutRepository;

    @Test
    void workServiceTest1() {

        DoWorkout doWorkout = new DoWorkout();
        DoWorkout dd = doWorkoutRepository.save(doWorkout);

    }
}