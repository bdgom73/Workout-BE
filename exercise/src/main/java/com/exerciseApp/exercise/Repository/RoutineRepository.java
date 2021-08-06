package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine,Long> {

    List<Routine> findByMember(Member member);
}
