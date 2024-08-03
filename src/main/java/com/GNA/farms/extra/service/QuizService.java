package com.GNA.farms.extra.service;

import com.example.quizApp.examModels.Category;
import com.example.quizApp.examModels.Quiz;

import java.util.List;
import java.util.Set;

public interface QuizService {
	   Quiz addQuiz(Quiz quiz);
	   Quiz updateQuiz(Quiz quiz);
	   Set<Quiz> getQuiz();
	   Quiz getQuiz(Long qid);
	   void  deleteQuiz(Long qid);
	   List<Quiz> getQuizzesOfCategory(Category category);
	   List<Quiz> getActiveQuizzes();
	   List<Quiz> getActiveQuizzesOfCategory(Category c);

}
