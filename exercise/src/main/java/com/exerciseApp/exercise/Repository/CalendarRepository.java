package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {
}
