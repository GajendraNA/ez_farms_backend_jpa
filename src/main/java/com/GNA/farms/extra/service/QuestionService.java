package com.GNA.farms.extra.service;

import com.example.quizApp.examModels.Question;
import com.example.quizApp.examModels.Quiz;

import java.util.Set;

public interface QuestionService {
	   Question addQuestion(Question question);
	   Question updateQuestion(Question question);
	   Set<Question> getQuestion();
	   Question getQuestion(Long qid);
	   Set<Question> getQuestionsOfQuiz(Quiz quiz);
	   void  deleteQuestion(Long qid);
}
