package com.telusko.quizService.entity;

import java.util.List;

import com.telusko.quizService.payload.QuestionWrapper;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class Quiz {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer quizId;
	
	private String title;
	
	@ElementCollection
	private List<Integer> queNumbers;
	
}
