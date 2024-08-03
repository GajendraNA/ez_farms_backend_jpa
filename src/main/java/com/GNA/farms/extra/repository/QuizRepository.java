package com.GNA.farms.extra.repository;

import com.example.quizApp.examModels.Category;
import com.example.quizApp.examModels.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
   List<Quiz> findBycategory(Category category);
   List<Quiz> findByActive(Boolean b);
   List<Quiz> findByCategoryAndActive(Category c,Boolean b);
}
