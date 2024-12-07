package com.telusko.quizService.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telusko.quizService.entity.Quiz;
import com.telusko.quizService.exception.ResourceNotFoundException;
import com.telusko.quizService.feign.QuestionFeign;
import com.telusko.quizService.payload.QuestionWrapper;
import com.telusko.quizService.payload.Response;
import com.telusko.quizService.payload.Result;
import com.telusko.quizService.repository.QuizRepository;

@Service
public class QuizService {

	@Autowired
	QuizRepository quizRepo;

	@Autowired
	QuestionFeign questionFeign;

	// Create Quiz
	public Quiz createQuiz(String category, int numQ, String title) {
		// Validate inputs
		if (category == null || category.isEmpty() || title == null || title.isEmpty() || numQ <= 0) {
			throw new ResourceNotFoundException("Invalid input parameters");
		}

		// Check if the title is already in use
		if (checkTitle(title)) {
			throw new ResourceNotFoundException("Title alraedy exist");// Title already exists
		}
		ResponseEntity<List<Integer>> responseEntity = questionFeign.getQuestionsForQuiz(category, numQ);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			List<Integer> queNumber = responseEntity.getBody();
			Quiz quiz = new Quiz();
		quiz.setTitle(title);				
			quiz.setQueNumbers(queNumber);
			Quiz saved = quizRepo.save(quiz);
			return saved;
		}
		return null;
	}

	// Get All Quiz
	public List<Quiz> getAllQuiz() {
		List<Quiz> quizzes = quizRepo.findAll();
		if (quizzes.isEmpty()) {
			return null;
		}
		return quizzes;
	}

	// Get Quiz By Id
	public Quiz getbyId(Quiz body) {
		Quiz quiz = quizRepo.findById(body.getQuizId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Quiz Id"));
		if (quiz.equals(null)) {
			return null;
		}
		return quiz;
	}

	// Get only questions from quiz
	public List<QuestionWrapper> getQuizQuestion(Quiz body) {
		Quiz quiz = quizRepo.findById(body.getQuizId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Quiz Id"));
		ResponseEntity<List<QuestionWrapper>> responseEntity = questionFeign.getQuestionsfromId(quiz.getQueNumbers());
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			List<QuestionWrapper> questionWrapper = responseEntity.getBody();
			return questionWrapper;
		}
		return null;
	}

	// Get Result
	public Result getResultOfQuiz(Integer quizId, List<Response> body) {
		quizRepo.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Invalid Quiz Id"));
		ResponseEntity<Result> responseEntity = questionFeign.getResult(body);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			Result result = responseEntity.getBody();
			result.setQuizId(quizId);
			return result;
		}
		return null;
	}

	public boolean checkTitle(String title) {
		Quiz quiz = quizRepo.findByTitle(title);
		if (quiz == null) {
			return false;
		}
		return true;
	}

}
