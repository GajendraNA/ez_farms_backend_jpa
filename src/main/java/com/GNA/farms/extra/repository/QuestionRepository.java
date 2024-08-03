package com.GNA.farms.extra.repository;

import com.example.quizApp.examModels.Question;
import com.example.quizApp.examModels.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	Set<Question> findByQuiz(Quiz quiz);

}
