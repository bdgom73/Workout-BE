package com.exerciseApp.exercise;

import com.exerciseApp.exercise.Entity.Workout;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ServletInitializer extends SpringBootServletInitializer {

	@PersistenceContext
	private EntityManager em;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ExerciseApplication.class);
	}

}
