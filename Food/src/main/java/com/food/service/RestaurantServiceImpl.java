package com.food.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.exception.AddressException;
import com.food.exception.CategoryException;
import com.food.exception.ItemException;
import com.food.exception.LoginException;
import com.food.exception.RestaurantException;
import com.food.model.Address;
import com.food.model.CurrentUserSession;
import com.food.model.Item;
import com.food.model.Restaurant;
import com.food.repository.AddressRepo;
import com.food.repository.CurrentUserSessionRepo;
import com.food.repository.ItemRepo;
import com.food.repository.RestaurantRepo;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepo resRepo;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private ItemRepo itemRepo;

	@Autowired
	private CurrentUserSessionRepo currSession;

	@Override
	public Restaurant addRestaurant(String key, Restaurant restaurant) throws RestaurantException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess != null && currSess.getRole().equalsIgnoreCase("admin")) {
			
			Restaurant existsRestaurant=resRepo.findByRestaurantName(restaurant.getRestaurantName());
			if (existsRestaurant != null)
				throw new RestaurantException("Restaurant already exists!");

			Address address = restaurant.getAddress();
			address.getRestaurantList().add(restaurant);

			List<Item> itemList = restaurant.getItemList();
			for (Item ele : itemList) {
				ele.getRestaurants().add(restaurant);
			}
			return resRepo.save(restaurant);
		} else
			throw new LoginException("Admin login required");
	}

	@Override
	public Restaurant updateRestaurant(String key, Restaurant restaurant) throws RestaurantException, LoginException {
		// TODO Auto-generated method stub

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess != null && currSess.getRole().equalsIgnoreCase("admin")) {

			Optional<Restaurant> opt = resRepo.findById(restaurant.getRestaurantId());
			if (opt.isPresent()) {
				return resRepo.save(restaurant);
			} else {
				throw new RestaurantException("Restaurant id not found!");
			}
		} else
			throw new LoginException("Admin login required");
	}

	@Override
	public Restaurant removeRestaurant(String key, Integer restaurantId) throws RestaurantException, LoginException {
		// TODO Auto-generated method stub

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess != null && currSess.getRole().equalsIgnoreCase("admin")) {

			Optional<Restaurant> opt = resRepo.findById(restaurantId);
			if (opt.isPresent()) {
				Restaurant restaurant = opt.get();
				resRepo.delete(restaurant);
				return restaurant;
			} else {
				throw new RestaurantException("Restaurant id not found!");
			}
		} else
			throw new LoginException("Admin login required");
	}

	@Override
	public Restaurant viewRestaurantById(String key, Integer restaurantId) throws RestaurantException, LoginException {
		// TODO Auto-generated method stub

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Optional<Restaurant> opt = resRepo.findById(restaurantId);
		if (opt.isPresent()) {
			Restaurant restaurant = opt.get();
			return restaurant;
		} else {
			throw new RestaurantException("Restaurant id not found!");
		}
	}

	@Override
	public List<Restaurant> getAllRestaurants(String key) throws RestaurantException, LoginException {
		// TODO Auto-generated method stub

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		List<Restaurant> restaurantList = resRepo.findAll();
		if (!restaurantList.isEmpty()) {
			return restaurantList;
		} else {
			throw new RestaurantException("Empty!");
		}
	}

	@Override
	public List<Restaurant> viewNearByRestaurant(String key, String city)
			throws RestaurantException, AddressException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Address address = addressRepo.findByCity(city);
		if (address != null) {
			List<Restaurant> restaurantList = address.getRestaurantList();
			if (!restaurantList.isEmpty()) {
				return restaurantList;
			} else {
				throw new RestaurantException("No restaurant found!");
			}
		} else {
			throw new AddressException("No address found!");
		}
	}

	@Override
	public List<Restaurant> viewRestaurantByItemName(String key, String itemName)
			throws RestaurantException, ItemException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Item item = itemRepo.findByItemName(itemName);
		if (item == null) {
			throw new ItemException("Item not found!");
		} else {
			List<Restaurant> restaurantList = item.getRestaurants();
			if (!restaurantList.isEmpty()) {
				return restaurantList;
			} else {
				throw new RestaurantException("Restaurant not found!");
			}
		}
	}

}
