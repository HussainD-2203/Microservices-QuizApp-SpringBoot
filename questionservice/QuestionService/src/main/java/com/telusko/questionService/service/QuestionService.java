package com.telusko.questionService.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telusko.questionService.entity.Question;
import com.telusko.questionService.exception.ResourceNotFoundException;
import com.telusko.questionService.payload.QuestionWrapper;
import com.telusko.questionService.payload.Response;
import com.telusko.questionService.payload.Result;
import com.telusko.questionService.repository.QuestionRepository;

import jakarta.transaction.Transactional;

@Service
public class QuestionService {

	@Autowired
	QuestionRepository questionRepo;

	// Get All Questions
	public List<Question> getAllQuestions() {
		List<Question> questions = questionRepo.findAll();
		if (questions.isEmpty()) {
			return null;
		}
		return questions;
	}

	// Get Question by category
	public List<Question> getByCategory(String category) {
		List<Question> questions = questionRepo.findByCategory(category);
		if (questions.isEmpty()) {
			return null;
		}
		return questions;
	}

	// Add question
	public Question addQuestion(Question body) {
		Question question = questionRepo.save(body);
		return question;
	}

	// Method to add or update
	@Transactional
	public Question addOrUpdateQuestion(Question body) {
		if (body == null) {
			return null;
		}

		// Save
		if (body.getQuestionId() == null || body.getQuestionId() == 0) {
			Question question = questionRepo.save(body);
			return question;
		}
		// Update
		else {
			Question existingQuestion = questionRepo.findById(body.getQuestionId())
					.orElseThrow(() -> new ResourceNotFoundException("Invalid question ID"));
			existingQuestion.setQuestionTitle(body.getQuestionTitle());
			existingQuestion.setOption1(body.getOption1());
			existingQuestion.setOption2(body.getOption2());
			existingQuestion.setOption3(body.getOption3());
			existingQuestion.setOption4(body.getOption4());
			existingQuestion.setCategory(body.getCategory());
			existingQuestion.setDifficultyLevel(body.getDifficultyLevel());
			Question updated = questionRepo.save(existingQuestion);
			return updated;
		}
	}

//	// Get QuestionBy Id
//	public Question getQuestionById(Integer questionId) {
//
//		Question question = questionRepo.findById(questionId)
//				.orElseThrow(() -> new ResourceNotFoundException("Invalid question Id"));
//		return question;
//	}

	// Get Question Numbers for Quiz
	public List<Integer> getQuestionsForQuiz(String category, int numQuestions) {

		if (category == null || category.isEmpty() || numQuestions <= 0) {
			throw new ResourceNotFoundException("Invalid input parameters");
		}

		List<Integer> questionIds = questionRepo.findByCategory(category, numQuestions);
		if (questionIds == null || questionIds.isEmpty()) {
			throw new ResourceNotFoundException("Data not found");
		}
		return questionIds;
	}

	//get questionWrapper from list of question Ids
	public List<QuestionWrapper> getQuestionsfromId(List<Integer> questionIds) {
		
		List<QuestionWrapper> questionWrappers = new ArrayList<>();
		for(int questionId:questionIds) {
			Question question = questionRepo.findById(questionId)
					.orElseThrow(() -> new ResourceNotFoundException("Invalid question Id"));
			QuestionWrapper questionWrapper = new QuestionWrapper(question.getQuestionId(),
					question.getQuestionTitle(),question.getOption1(),question.getOption2(),
					question.getOption3(),question.getOption4());	
			questionWrappers.add(questionWrapper);
		}
		return questionWrappers;
	}
	
	//Get Result
	public Result getResult(List<Response> responses) {
		int i=0;
		int right=0;
		for(Response response:responses) {
			Question question = questionRepo.findById(response.getQuestionId())
					.orElseThrow(() -> new ResourceNotFoundException("Invalid question Id"));
			if(response.getAnswer().equals(question.getRightAnswer())) {
				right++;
			}
			i++;
		}
		Result result = new Result(null,i,right,right*2);
		return result;
	}

}
