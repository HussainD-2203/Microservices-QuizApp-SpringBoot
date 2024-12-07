package com.telusko.quizService.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
	
	Integer quizId;
	int totalNumberOfQuestions;
	int correctAnswer;
	int marksObtained;
	
}
