package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.DoWorkout;
import com.exerciseApp.exercise.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoWorkoutRepository extends JpaRepository<DoWorkout, Long> {

    List<DoWorkout> findByMemberAndDateBetween(Member member, LocalDate start, LocalDate end);

    Optional<DoWorkout> findByMemberAndDate(Member member, LocalDate date);

}
