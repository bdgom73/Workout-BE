package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.WorkoutDTO.RoutineRegister;
import com.exerciseApp.exercise.DTO.WorkoutDTO.VolumeRegister;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Entity.Volume;
import com.exerciseApp.exercise.Entity.Workout;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Repository.RoutineRepository;
import com.exerciseApp.exercise.Repository.WorkoutRepository;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class WorkoutServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired WorkoutService workoutService;
    @Autowired WorkoutRepository workoutRepository;
    @Autowired CreateIDService createIDService;
    @Autowired
    RoutineRepository routineRepository;
    @Test
    void workServiceTest1(){
        Member member = new Member();
        member.setEmail("test@email");
        memberRepository.save(member);

        Workout workoutA = new Workout();
        workoutA.setName("WorkoutA");
        Workout workoutB = new Workout();
        workoutB.setName("WorkoutB");

        workoutRepository.save(workoutA);
        workoutRepository.save(workoutB);

        VolumeRegister v1 = VolumeRegister.builder().num(10).sets(10).orders(0)
                .workout_id(workoutA.getId()).build();

        VolumeRegister v2 = VolumeRegister.builder().num(10).sets(10).orders(1)
                .workout_id(workoutB.getId()).build();

        List<VolumeRegister> vrs = new ArrayList<>();
        vrs.add(v1); vrs.add(v2);

        RoutineRegister register = RoutineRegister.builder().part("Test").title("테스트")
                .volumes(vrs).build();
        Long aLong = workoutService.addRoutine(register, member);

        Optional<Routine> aaaa = routineRepository.findById(aLong);
        List<Volume> volumes = aaaa.get().getVolumes();
        for (Volume volume : volumes) {
            System.out.println("volume.getId() = " + volume.getId());
            System.out.println(" volume.getWorkout().getName() = " +  volume.getWorkout().getName());

        }
    }
}