package com.telusko.questionService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telusko.questionService.entity.Question;
import com.telusko.questionService.payload.ErrorResponse;
import com.telusko.questionService.payload.QuestionWrapper;
import com.telusko.questionService.payload.Response;
import com.telusko.questionService.payload.Result;
import com.telusko.questionService.service.QuestionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	QuestionService questionservice;
	
	@Autowired
	Environment enviornment;
	
	//get all questions
	@GetMapping("/allquestions")
	public ResponseEntity<Object> getAllQuestion(){
		List<Question> questions = questionservice.getAllQuestions();
		if(questions==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Data Not Found",HttpStatus.NOT_FOUND.value());
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(questions);
	}
	
	// get questions by category
	@GetMapping("/questionByCategory")
	public ResponseEntity<Object> getByCategory(@RequestBody Question body){
		List<Question> questions = questionservice.getByCategory(body.getCategory());
		if(questions==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Enter valid category",HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}		
		return ResponseEntity.status(HttpStatus.OK).body(questions);		
	}
	
	//Add or Update question
	@PostMapping("/addOrUpdateQuestion")
	public ResponseEntity<Object> addQuestion(@Valid @RequestBody Question body){
		Question question = questionservice.addOrUpdateQuestion(body);
		if(question==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Question addition failed",HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(question);
	}
	
//	//get Questions By questionID
//	@GetMapping("/getQuestionById/{questionId}")
//	public ResponseEntity<Object> getQuestionById(@PathVariable Integer questionId){
//		Question question = questionservice. getQuestionById(questionId);
//		if(question==null) {
//			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Question addition failed",HttpStatus.BAD_REQUEST.value());
//			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(question);
//	}

	
	//Get Question Numbers for Quiz
	@GetMapping("/generate")
	public ResponseEntity<Object> getQuestionsForQuiz(@RequestParam String category ,@RequestParam int numQuestions){
		List<Integer> questionIds = questionservice.getQuestionsForQuiz(category,numQuestions);
		return ResponseEntity.status(HttpStatus.OK).body(questionIds);
	}
	
	//get question wrapper from list of question Ids
	@PostMapping("/getQuestions")
	public ResponseEntity<Object> getQuestionsfromId(@RequestBody List<Integer> questionIds){
		System.out.println(enviornment.getProperty("local.server.port"));
		List<QuestionWrapper> questionWrappers = questionservice.getQuestionsfromId(questionIds);
		if(questionWrappers==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Enter valid credentials",HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}		
		return ResponseEntity.status(HttpStatus.OK).body(questionWrappers);	}
	
	//Get Result
	@PostMapping("/getResult")
	public ResponseEntity<Object> getResult(@RequestBody  List<Response> response){
		Result result = questionservice.getResult(response);
		if(result==null) {
			ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),"Enter valid credentials",HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	
}
