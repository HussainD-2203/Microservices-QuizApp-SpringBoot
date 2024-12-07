package com.telusko.quizService.payload;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QuizDto {
	String category;
	
	Integer numQuestions;
	
	String title;
}
