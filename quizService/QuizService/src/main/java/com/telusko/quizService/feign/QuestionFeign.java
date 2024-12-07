package com.telusko.quizService.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.telusko.quizService.payload.QuestionWrapper;
import com.telusko.quizService.payload.Response;
import com.telusko.quizService.payload.Result;

//Here we take methods of question Controller from question from questionService Micro-service and 
@FeignClient("QUESTIONSERVICE")//here we have to add name of the micro-service which we are calling
public interface QuestionFeign {

	// Get Question Numbers for Quiz
	@GetMapping("/question/generate")
	public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category,
			@RequestParam int numQuestions);

	//get question wrapper from list of question Ids
	@PostMapping("/question/getQuestions")
	public ResponseEntity<List<QuestionWrapper>> getQuestionsfromId(@RequestBody List<Integer> questionIds);
	
	//Get Result
	@PostMapping("/question/getResult")
	public ResponseEntity<Result> getResult(@RequestBody List<Response> response);
	
}
