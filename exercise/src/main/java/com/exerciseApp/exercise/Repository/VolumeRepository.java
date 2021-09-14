package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Entity.Volume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolumeRepository extends JpaRepository<Volume, Long> {

    List<Volume> findByRoutine(Routine routine);
}
