package com.telusko.quizService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telusko.quizService.entity.Quiz;
import com.telusko.quizService.payload.ErrorResponse;
import com.telusko.quizService.payload.QuestionWrapper;
import com.telusko.quizService.payload.QuizDto;
import com.telusko.quizService.payload.Response;
import com.telusko.quizService.payload.Result;
import com.telusko.quizService.service.QuizService;


@RestController
@RequestMapping("/quiz")
public class QuizController {
	
	@Autowired
	QuizService quizService;
	
	//Create Quiz
	@PostMapping("/create")
	public ResponseEntity<Object> createQuiz(@RequestBody QuizDto body){		
		Quiz quiz = quizService.createQuiz(body.getCategory(),body.getNumQuestions(),body.getTitle()); 		
		if(quiz==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
					"Quiz creation failed ",HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(quiz);
	}
	
	
	//Get All Quiz
	@GetMapping("/getAll")
	public ResponseEntity<Object> getALlQuiz(){
		List<Quiz> quizzes = quizService.getAllQuiz(); 
		if(quizzes==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
					"Data Not Found",HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(quizzes);
	}
	
	//Get Quiz by id
	@PostMapping("/getById")
	public ResponseEntity<Object> getById(@RequestBody Quiz body){
		Quiz quiz = quizService.getbyId(body);	
		if(quiz==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
					"Data Not Found",HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(quiz);
	}
	
	//Get Questions only from Quiz
	@PostMapping("/getQuizQuestion")
	public ResponseEntity<Object> getQuizQuestion(@RequestBody Quiz body){
		List <QuestionWrapper> questions = quizService.getQuizQuestion(body);
		if(questions.isEmpty()) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
					"Data Not Found",HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(questions);
	}
	
	//Submit API for Quiz answers
	@PostMapping("/submit/{quizId}")
	public ResponseEntity<Object> getResultOfQuiz(@PathVariable Integer quizId, @RequestBody List<Response> body){
		Result result = quizService.getResultOfQuiz(quizId,body);
		if(result==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
					"Data Not Found",HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	
}
