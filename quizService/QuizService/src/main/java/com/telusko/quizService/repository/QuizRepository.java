package com.telusko.quizService.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.telusko.quizService.entity.Quiz;


public interface QuizRepository extends JpaRepository<Quiz, Integer>{

	public Quiz findByTitle(String title);
	
}
