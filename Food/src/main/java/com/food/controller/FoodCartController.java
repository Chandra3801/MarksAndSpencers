package com.food.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.exception.CustomerException;
import com.food.exception.FoodCartException;
import com.food.exception.ItemException;
import com.food.exception.LoginException;
import com.food.model.CustomerDTO;
import com.food.model.FoodCart;
import com.food.model.ItemDTO;
import com.food.service.FoodCartService;

@RestController
@RequestMapping("/foodcart")
public class FoodCartController {

	@Autowired
	private FoodCartService foodCartService;

	@PostMapping("/addtocart/{customerId}")
	public ResponseEntity<FoodCart> addItemToCartHandler(@RequestParam(required = false) String key,
			@PathVariable("customerId") Integer customerId, @RequestParam(required = false) Integer itemId)
			throws ItemException, CustomerException, LoginException {
		FoodCart foodCart = foodCartService.addItemToCart(key, customerId, itemId);
		return new ResponseEntity<FoodCart>(foodCart, HttpStatus.OK);
	}

	@PutMapping("/increaseQuantity")
	public ResponseEntity<FoodCart> increaseItemQuantityHandler(@RequestParam(required = false) String key,
			@RequestParam Integer cartId, @RequestParam Integer quantity, @RequestParam Integer itemId)
			throws ItemException, CustomerException, FoodCartException, LoginException {
		FoodCart foodCart = foodCartService.increaseItemQuantity(key, cartId, quantity, itemId);
		return new ResponseEntity<FoodCart>(foodCart, HttpStatus.OK);
	}

	@PutMapping("/decreaseQuantity")
	public ResponseEntity<FoodCart> decreaseItemQuantityHandler(@RequestParam(required = false) String key,
			@RequestParam Integer cartId, @RequestParam Integer quantity, @RequestParam Integer itemId)
			throws ItemException, CustomerException, FoodCartException, LoginException {
		FoodCart foodCart = foodCartService.decreaseItemQuantity(key, cartId, quantity, itemId);
		return new ResponseEntity<FoodCart>(foodCart, HttpStatus.OK);
	}

	@DeleteMapping("/item")
	public ResponseEntity<FoodCart> removeItemHandler(@RequestParam(required = false) String key,
			@RequestParam Integer cartId, @RequestParam Integer itemId)
			throws ItemException, CustomerException, FoodCartException, LoginException {
		FoodCart foodCart = foodCartService.removeItem(key, cartId, itemId);
		return new ResponseEntity<FoodCart>(foodCart, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<FoodCart> clearCartHandler(@RequestParam(required = false) String key,
			@RequestParam Integer cartId) throws ItemException, CustomerException, FoodCartException, LoginException {
		FoodCart foodCart = foodCartService.removeCart(key, cartId);
		return new ResponseEntity<FoodCart>(foodCart, HttpStatus.OK);
	}

}
