package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.DTO.WorkoutDTO.RecommendRoutineDTO;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Recommend;
import com.exerciseApp.exercise.Entity.Routine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    Long countByRoutineAndIsRecommend(Routine routine, Boolean isRecommend);

    Optional<Recommend> findByRoutineAndMember(Routine routine, Member member);

    Optional<Recommend> findByRoutine(Routine routine);

    @Query("SELECT r.routine as routine , COUNT(r) as cnt FROM " +
            "Recommend r Where r.routine = :routine AND r.isRecommend = :isRecommend " +
            "Group by r.routine")
    Optional<RecommendRoutineDTO> findByRoutine(@Param("routine") Routine routine, @Param("isRecommend") Boolean isRecommend);

    @Query("SELECT r.routine as routine , COUNT(r) as cnt FROM " +
            "Recommend r Where r.isRecommend = :isRecommend " +
            "Group by r.routine")
    Page<RecommendRoutineDTO> findPageByRecommendRoutine(@Param("isRecommend") Boolean isRecommend, Pageable pageable);

    @Query("SELECT r.routine as routine , COUNT(r) as cnt FROM " +
            "Recommend r Where r.routine.share = :share " +
            "Group by r.routine")
    Page<RecommendRoutineDTO> findPageByShareRoutine(@Param("share") Boolean share, Pageable pageable);

    @Query("SELECT r.routine as routine , COUNT(r) as cnt FROM " +
            "Recommend r Where r.routine.id = :routine_id and r.routine.share = :share " +
            "Group by r.routine")
    Optional<RecommendRoutineDTO> findByRoutineDetail(@Param("routine_id") Long routine_id, @Param("share") Boolean share);

    @Query("SELECT r.routine as routine , COUNT(case when r.isRecommend = true then 1 end) as cnt FROM " +
            "Recommend r Where r.routine.share = true " +
            "Group by r.routine ")
    Page<RecommendRoutineDTO> findPageByRecommendRoutine(Pageable pageable);
}
