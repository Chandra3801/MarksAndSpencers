package com.food.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.exception.LoginException;
import com.food.model.Admin;
import com.food.model.CurrentUserSession;
import com.food.model.Customer;
import com.food.model.Login;
import com.food.model.LoginDTO;
import com.food.repository.AdminRepo;
import com.food.repository.CurrentUserSessionRepo;
import com.food.repository.CustomerRepo;
import com.food.repository.LoginRepo;

import net.bytebuddy.utility.RandomString;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private CurrentUserSessionRepo sessionRepo;
	
	@Autowired
	private LoginRepo loginRepo;

	@Override
	public String loginAccount(LoginDTO loginDTO) throws LoginException {

		if (loginDTO.getRole().equalsIgnoreCase("customer")) {

			Customer customer = customerRepo.findByEmail(loginDTO.getEmail());
			if (customer == null)
				throw new LoginException("Invalid email");

			if (customer.getPassword().equals(loginDTO.getPassword())) {

				CurrentUserSession cuurSession = sessionRepo.findByEmail(loginDTO.getEmail());

				if (cuurSession != null)
					throw new LoginException("User already logged-In!");

				CurrentUserSession currentUserSession = new CurrentUserSession();
				currentUserSession.setEmail(loginDTO.getEmail());
				currentUserSession.setLoginDateTime(LocalDateTime.now());
				currentUserSession.setRole("customer");
				String privateKey = RandomString.make(6);
				currentUserSession.setPrivateKey(privateKey);
				
				Login login=new Login();
				login.setEmail(loginDTO.getEmail());
				login.setPassword(loginDTO.getPassword());
				login.setRole(loginDTO.getRole());
				
				
				loginRepo.save(login);
				sessionRepo.save(currentUserSession);
				return "Login Sucessufull!";
			} else {
				throw new LoginException("Please Enter a valid password");
			}

		} else if (loginDTO.getRole().equalsIgnoreCase("admin")) {
			Admin admin = adminRepo.findByEmail(loginDTO.getEmail());
			if (admin == null)
				throw new LoginException("Invalid email");

			if (admin.getPassword().equals(loginDTO.getPassword())) {

				CurrentUserSession cuurSession = sessionRepo.findByEmail(loginDTO.getEmail());

				if (cuurSession != null)
					throw new LoginException("User already logged-In!");

				CurrentUserSession currentUserSession = new CurrentUserSession();
				currentUserSession.setEmail(loginDTO.getEmail());
				currentUserSession.setLoginDateTime(LocalDateTime.now());
				currentUserSession.setRole("admin");
				String privateKey = RandomString.make(6);
				currentUserSession.setPrivateKey(privateKey);
				
				Login login=new Login();
				login.setEmail(loginDTO.getEmail());
				login.setPassword(loginDTO.getPassword());
				login.setRole(loginDTO.getRole());
				
				
				loginRepo.save(login);

				sessionRepo.save(currentUserSession);
				return "Login Sucessufull!";
			} else {
				throw new LoginException("Please Enter a valid password");
			}
		}
		return null;
	}

	@Override
	public String logoutAccount(String role, String key) throws LoginException {

		if (role.equalsIgnoreCase("customer")) {

			CurrentUserSession currSession = sessionRepo.findByPrivateKey(key);
			if (currSession == null)
				throw new LoginException("Invalid key");

			if (currSession.getRole().equalsIgnoreCase("customer")) {

				sessionRepo.delete(currSession);
				return "Logged Out!";

			} else
				throw new LoginException("Invalid role");

		} else if (role.equalsIgnoreCase("admin")) {

			CurrentUserSession currSession = sessionRepo.findByPrivateKey(key);
			if (currSession == null)
				throw new LoginException("Invalid key");

			if (currSession.getRole().equalsIgnoreCase("admin")) {

				sessionRepo.delete(currSession);
				return "Logged Out!";

			} else
				throw new LoginException("Invalid role");

		} else
			throw new LoginException("Invalid role");
	}
}
