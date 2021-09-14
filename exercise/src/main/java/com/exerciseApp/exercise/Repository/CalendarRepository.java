package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Calendar;
import com.exerciseApp.exercise.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByMemberAndStartDateBetween(Member member, LocalDate start, LocalDate end);

    Optional<Calendar> findByMemberAndStartDateAndEndDate(Member member, LocalDate startDate, LocalDate endDate);

    Optional<Calendar> findByIdAndMember(Long id, Member member);


}
