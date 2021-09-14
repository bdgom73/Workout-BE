package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    List<WeightLog> findByDateBetweenAndMember(LocalDateTime start, LocalDateTime end, Member member);

}
