package com.GNA.farms.extra.controller;


import com.example.quizApp.examModels.Question;
import com.example.quizApp.examModels.Quiz;
import com.example.quizApp.service.QuestionService;
import com.example.quizApp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
@RestController
@RequestMapping("/question")
@CrossOrigin("*")
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuizService quizService;
	
	//add question
	@PostMapping("/")
	public  ResponseEntity<Question> addQuestion(@RequestBody Question question)
	{
	 Question question1=this.questionService.addQuestion(question);
	 return ResponseEntity.ok(question1);
	}
	
	//update question
	@PutMapping("/")
	public ResponseEntity<Question> upadateQuestion(@RequestBody Question question)
	{
	return ResponseEntity.ok(this.questionService.updateQuestion(question));
	}
	
	//get All questions of quiz id any
	@GetMapping("/quiz/{qid}")
	public ResponseEntity<?> getQuestionOfQuiz(@PathVariable("qid") Long qid)
	{
	Quiz quiz=this.quizService.getQuiz(qid);
	Set<Question>questions=quiz.getQuestions();
	List list=new ArrayList(questions);
	if(list.size()>Integer.parseInt(quiz.getNoOfQuestions()))
	{
	 list=list.subList(0, Integer.parseInt(quiz.getNoOfQuestions()+1));
	}
	Collections.shuffle(list);
	return ResponseEntity.ok(list);
	}
	
	//get All questions
	@GetMapping("/quiz/all/{qid}")
	public ResponseEntity<?> getQuestionOfQuizAdmin(@PathVariable("qid") Long qid)
	{
    Quiz quiz=new Quiz();
    quiz.setQid(qid);
    Set<Question>questions=this.questionService.getQuestionsOfQuiz(quiz);
	return ResponseEntity.ok(questions);
	}
	
	//get single question
	@GetMapping("/{quesId}")
	public Question get(@PathVariable("quesId") Long quesId)
	{
	return this.questionService.getQuestion(quesId);
	}
	
	
	//delete question
	@DeleteMapping("/{qId}")
	public void deleteQuetion(@PathVariable("qId") Long qId)
	{
	this.questionService.deleteQuestion(qId);
	}
	
	
}
