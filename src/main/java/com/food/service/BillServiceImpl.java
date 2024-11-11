package com.food.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.exception.BillException;
import com.food.exception.CustomerException;
import com.food.exception.LoginException;
import com.food.exception.OrderDetailsException;
import com.food.model.Bill;
import com.food.model.CurrentUserSession;
import com.food.model.Customer;
import com.food.model.FoodCart;
import com.food.model.Item;
import com.food.model.OrderDetails;
import com.food.model.OrderHistory;
import com.food.repository.BillRepo;
import com.food.repository.CurrentUserSessionRepo;
import com.food.repository.CustomerRepo;
import com.food.repository.FoodCartRepo;
import com.food.repository.ItemRepo;
import com.food.repository.OrderDetailsRepo;
import com.food.repository.OrderHistoryRepo;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private ItemRepo itemRepo;

	@Autowired
	private BillRepo billRepo;

	@Autowired
	private OrderDetailsRepo orderDetailRepo;

	@Autowired
	private CustomerRepo cusDAO;

	@Autowired
	private FoodCartRepo foodCartRepo;

	@Autowired
	private CurrentUserSessionRepo currSession;

	@Autowired
	private OrderHistoryRepo orderHistoryRepo;

	@Override
	public Bill generateBill(String key, Integer customerId, Integer orderDetailId)
			throws CustomerException, OrderDetailsException, LoginException, BillException {

		// user validation
		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		// order validation
		Optional<OrderDetails> opt = orderDetailRepo.findById(orderDetailId);
		if (opt.isEmpty())
			throw new OrderDetailsException("order details not found ...");

		// existing generate bill check
		if (opt.get().getOrderStatus().equals("completed"))
			throw new BillException("Bill already generated for this order id");

		// customer validation
		Optional<Customer> customerOpt = cusDAO.findById(customerId);
		if (customerOpt.isEmpty())
			throw new CustomerException("customer does not exist");

		OrderDetails orderDetails = opt.get();
		FoodCart foodCart = orderDetails.getFoodCart();

		Double totalCost = 0D;
		Integer totalItems = 0;
		for (int i = 0; i < foodCart.getItemList().size(); i++) {
			Item items = foodCart.getItemList().get(i);
			totalCost += (items.getQuantity() * items.getCost());
			totalItems += items.getQuantity();
			items.setQuantity(1);
			itemRepo.save(items);
		}

		Bill bill = new Bill();
		bill.setTotalCost(totalCost);
		bill.setTotalItem(totalItems);
		bill.setBillDate(LocalDateTime.now());
		bill.setOrder(orderDetails);
		billRepo.save(bill);

		// transfer bill to order history list
		OrderHistory orderHis = new OrderHistory();
		orderHis.setBill(bill);
		orderHis.setCustomerId(customerId);		
		orderHistoryRepo.save(orderHis);

		orderDetails.setOrderStatus("completed");
		orderDetailRepo.save(orderDetails);
		
		foodCartRepo.save(foodCart);		
		return bill;
	}

	@Override
	public Bill updateBill(String key, Bill bill) throws BillException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Optional<Bill> opt = billRepo.findById(bill.getBillId());
		if (opt.isPresent()) {
			return billRepo.save(bill);
		} else {
			throw new BillException("Bill doesn't exists..");
		}
	}

	@Override
	public Bill removeBill(String key, Integer billId) throws BillException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Optional<Bill> opt = billRepo.findById(billId);
		if (opt.isPresent()) {
			Bill bill = opt.get();
			billRepo.delete(bill);
			return bill;
		} else {
			throw new BillException("Bill not found with ID: " + billId);
		}

	}

	@Override
	public Bill viewBill(String key, Integer billId) throws BillException, LoginException {

		CurrentUserSession currSess = currSession.findByPrivateKey(key);
		if (currSess == null)
			throw new LoginException("Login required");

		Optional<Bill> opt = billRepo.findById(billId);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new BillException("Bill not found with ID: " + billId);
		}
	}

}
