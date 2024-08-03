package com.GNA.farms.extra.service;

import com.example.quizApp.examModels.Category;

import java.util.Set;

public interface CategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category);
    Set<Category> getCategories();
    Set<Category> getCategoriesSortedByName();
    Category getCategory(Long cid);
    void  deleteCategory(Long cid);
}
