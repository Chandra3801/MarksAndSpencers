package com.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer>{
	
	public Customer findByEmail(String email);

}
