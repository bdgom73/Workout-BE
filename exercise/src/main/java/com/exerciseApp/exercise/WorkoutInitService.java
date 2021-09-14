package com.exerciseApp.exercise;

import com.exerciseApp.exercise.Repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class WorkoutInitService {

    @Autowired
    WorkoutRepository workoutRepository;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {

//        String q = "INSERT INTO WORKOUT (workout_id,e_type,part,name)  SELECT * FROM CSVREAD('C:/Users/bdgom/OneDrive/desktop/운동.csv',null,'charset=UTF-8');";
//        em.createNativeQuery(q, Workout.class).executeUpdate();


    }
}
