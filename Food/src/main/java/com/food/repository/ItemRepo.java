package com.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.exception.ItemException;
import com.food.model.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer>{
	
	public Item findByItemName(String item) throws ItemException;
	
	
}
