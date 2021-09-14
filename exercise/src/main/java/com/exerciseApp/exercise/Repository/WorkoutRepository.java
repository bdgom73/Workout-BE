package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("SELECT w FROM Workout w where w.e_type = :e_type")
    List<Workout> findByWortOutType(Integer e_type);

    @Query("SELECT w FROM Workout w where w.e_type = :e_type")
    Page<Workout> findPageByE_type(Integer e_type, Pageable pageable);

    @Query("SELECT w FROM Workout w where w.e_type = :e_type")
    Slice<Workout> findSliceByE_type(Integer e_type, Pageable pageable);

    List<Workout> findByPart(String part);

    Page<Workout> findPageByPart(String part, Pageable pageable);

    Slice<Workout> findSliceByPart(String part, Pageable pageable);


//    Long countByAll();
}
