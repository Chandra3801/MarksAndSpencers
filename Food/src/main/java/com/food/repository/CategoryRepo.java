package com.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.exception.CategoryException;
import com.food.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

	public Category findByCategoryName(String categoryName) throws CategoryException;

	@Query("from Category where categoryName=?1")
	public Category getCategoryByName(String categoryName);

}
