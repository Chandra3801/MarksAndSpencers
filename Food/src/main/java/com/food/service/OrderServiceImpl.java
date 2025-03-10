package com.food.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.exception.CustomerException;
import com.food.exception.FoodCartException;
import com.food.exception.ItemException;
import com.food.exception.LoginException;
import com.food.exception.OrderDetailsException;
import com.food.model.CurrentUserSession;
import com.food.model.Customer;
import com.food.model.FoodCart;
import com.food.model.Item;
import com.food.model.OrderDetails;
import com.food.model.Restaurant;
import com.food.repository.CurrentUserSessionRepo;
import com.food.repository.CustomerRepo;
import com.food.repository.OrderDetailsRepo;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDetailsRepo orderRepo;

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private CurrentUserSessionRepo currSession;

	@Override
	public OrderDetails addOrder(String key, Integer customerId) throws CustomerException, FoodCartException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Optional<Customer> opt = customerRepo.findById(customerId);
		if (opt.isEmpty())
			throw new CustomerException("customer does not exist..!");
		Customer customer = opt.get();
		FoodCart foodCart = customer.getCart();
		List<Item> itemList = foodCart.getItemList();
		if (itemList.isEmpty())
			throw new FoodCartException("cart is empty..!");
		List<OrderDetails> orderDetailsList = orderRepo.findAll();
		boolean flag = true;
		OrderDetails orderDetails = null;
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OrderDetails exOrderDetails = orderDetailsList.get(i);
			if (exOrderDetails.getFoodCart().getCartId() == foodCart.getCartId()
					&& exOrderDetails.getOrderStatus().equals("Pending")) {
				exOrderDetails.setFoodCart(foodCart);
				orderDetails = exOrderDetails;
				flag = false;
			}
		}

		if (flag) {
			orderDetails = new OrderDetails();
			orderDetails.setFoodCart(foodCart);
			orderDetails.setOrderDate(LocalDateTime.now());
			orderDetails.setOrderStatus("Pending");
		}
		orderRepo.save(orderDetails);
		return orderDetails;

	}

	@Override
	public OrderDetails updateOrder(String key, Integer orderId, Integer customerId)
			throws OrderDetailsException, CustomerException, FoodCartException, LoginException {
		
		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");
		
		Optional<OrderDetails> opt1 = orderRepo.findById(orderId);
		if (opt1.isPresent()) {
			Optional<Customer> opt = customerRepo.findById(customerId);
			if (opt.isEmpty())
				throw new CustomerException("customer does not exist..!");
			Customer customer = opt.get();
			FoodCart foodCart = customer.getCart();
			List<Item> itemList = foodCart.getItemList();
			if (itemList.isEmpty())
				throw new FoodCartException("cart is empty..!");
			List<OrderDetails> orderDetailsList = orderRepo.findAll();
			boolean flag = true;
			OrderDetails orderDetails = null;
			for (int i = 0; i < orderDetailsList.size(); i++) {
				OrderDetails exOrderDetails = orderDetailsList.get(i);
				if (exOrderDetails.getFoodCart().getCartId() == foodCart.getCartId()) {
					exOrderDetails.setFoodCart(foodCart);
					orderDetails = exOrderDetails;
					flag = false;
				}
			}

			if (flag) {
				orderDetails = new OrderDetails();
				orderDetails.setFoodCart(foodCart);
				orderDetails.setOrderDate(LocalDateTime.now());
				orderDetails.setOrderStatus("pending..!");
			}
			orderRepo.save(orderDetails);
			return orderDetails;
		} else {
			throw new OrderDetailsException("order does not exist...!");
		}
	}

	@Override
	public OrderDetails removeOrder(String key, Integer orderId) throws OrderDetailsException, LoginException {
		
		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");
		
		Optional<OrderDetails> opt = orderRepo.findById(orderId);
		if (opt.isPresent()) {
			OrderDetails deletedOrder = opt.get();
			orderRepo.delete(deletedOrder);
			return deletedOrder;
		} else {
			throw new OrderDetailsException("order does not exist...!");
		}
	}

	@Override
	public OrderDetails viewOrder(String key, Integer orderId) throws OrderDetailsException, LoginException {
		
		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");
		
		Optional<OrderDetails> opt = orderRepo.findById(orderId);
		if (opt.isPresent()) {
			OrderDetails order = opt.get();
			return order;
		} else {
			throw new OrderDetailsException("order does not exist...!");
		}
	}

}
