package com.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.model.Login;

public interface LoginRepo extends JpaRepository<Login, Integer> {
	public Login findByEmail(String email);

	public Login findByPassword(String password);
}
