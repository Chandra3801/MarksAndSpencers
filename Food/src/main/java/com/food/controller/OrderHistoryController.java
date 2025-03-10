package com.food.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.exception.CustomerException;
import com.food.exception.LoginException;
import com.food.exception.OrderHistoryException;
import com.food.exception.RestaurantException;
import com.food.model.OrderHistory;
import com.food.service.OrderHistoryService;

@RestController
@RequestMapping("/order-history")
public class OrderHistoryController {

	@Autowired
	private OrderHistoryService orderHisService;

	@GetMapping("/get")
	public ResponseEntity<OrderHistory> OrderHistoryById(@RequestParam(required = false) String key,
			@RequestParam(required = false) Integer orderHisId)
			throws RestaurantException, LoginException, OrderHistoryException {
		OrderHistory orderHistory = orderHisService.getOrderHistoryById(key, orderHisId);
		return new ResponseEntity<OrderHistory>(orderHistory, HttpStatus.ACCEPTED);
	}

	@GetMapping("/customer-id")
	public ResponseEntity<List<OrderHistory>> OrderHistoryByCustomerId(@RequestParam(required = false) String key,
			@RequestParam(required = false) Integer customerId)
			throws RestaurantException, LoginException, OrderHistoryException, CustomerException {
		List<OrderHistory> orderHistoryList = orderHisService.getOrderHistoryByCustomerId(key, customerId);
		return new ResponseEntity<List<OrderHistory>>(orderHistoryList, HttpStatus.ACCEPTED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<OrderHistory>> getAllOrderHistory(@RequestParam(required = false) String key)
			throws RestaurantException, LoginException, OrderHistoryException, CustomerException {
		List<OrderHistory> orderHistoryList = orderHisService.getAllOrderHistory(key);
		return new ResponseEntity<List<OrderHistory>>(orderHistoryList, HttpStatus.ACCEPTED);
	}

}
