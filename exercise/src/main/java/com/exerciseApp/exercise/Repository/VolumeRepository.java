package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Volume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolumeRepository extends JpaRepository<Volume,Long> {
}
