package com.GNA.farms.extra.controller;

import com.example.quizApp.examModels.Category;
import com.example.quizApp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	//add category
	@PostMapping("/")
	public  ResponseEntity<Category> addCategory(@RequestBody Category category)
	{
	 Category category1=this.categoryService.addCategory(category);
	 return ResponseEntity.ok(category1);
	}
	//get Category
	@GetMapping("/{categoryId}")
	public Category getCategory(@PathVariable("categoryId") Long categoryId)
	{
	 return this.categoryService.getCategory(categoryId);
	}
	//get All Categories
	@GetMapping("/")
	public ResponseEntity<?> getCategories()
	{
	return ResponseEntity.ok(this.categoryService.getCategories());
	}
	//Filter sorted way
	@GetMapping("/sorted")
	public ResponseEntity<Set<Category>> getSortedCategories() {
	Set<Category> categories = categoryService.getCategoriesSortedByName();
	return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	//update category
	@PutMapping("/")
	public ResponseEntity<Category> updateCategory(@RequestBody Category category)
	{
	return ResponseEntity.ok(categoryService.updateCategory(category));
	}
	
	//delete Category
	@DeleteMapping("/{categoryId}")
	public void deleteCategory(@PathVariable("categoryId") Long categoryId)
	{
	this.categoryService.deleteCategory(categoryId);
	}

}
