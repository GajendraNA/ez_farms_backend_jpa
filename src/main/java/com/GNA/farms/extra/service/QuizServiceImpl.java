package com.GNA.farms.extra.service;

import com.example.quizApp.examModels.Category;
import com.example.quizApp.examModels.Quiz;
import com.example.quizApp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class QuizServiceImpl implements QuizService {
    
	@Autowired
	private QuizRepository quizRepository;
	
	@Override
	public Quiz addQuiz(Quiz quiz) {
		// TODO Auto-generated method stub
		return quizRepository.save(quiz);
	}

	@Override
	public Quiz updateQuiz(Quiz quiz) {
		// TODO Auto-generated method stub
		return quizRepository.save(quiz);
	}

	@Override
	public Set<Quiz> getQuiz() {
		// TODO Auto-generated method stub
		return new HashSet<Quiz>( quizRepository.findAll() );
	}

	@Override
	public Quiz getQuiz(Long qid) {
		// TODO Auto-generated method stub
		return quizRepository.findById(qid).get();
	}

	@Override
	public void deleteQuiz(Long qid) {
		// TODO Auto-generated method stub
		quizRepository.deleteById(qid);
		
	}

	@Override
	public List<Quiz> getQuizzesOfCategory(Category category) {
		// TODO Auto-generated method stub
		return this.quizRepository.findBycategory(category);
	}

	//get Active quizzes
	@Override
	public List<Quiz> getActiveQuizzes() {
		// TODO Auto-generated method stub
		return this.quizRepository.findByActive(true);
	}

	@Override
	public List<Quiz> getActiveQuizzesOfCategory(Category c) {
		// TODO Auto-generated method stub
		return this.quizRepository.findByCategoryAndActive(c, true);
	}

}
