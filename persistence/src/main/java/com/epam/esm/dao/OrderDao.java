package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderedGiftCertificate;
import com.epam.esm.entity.Pagination;

/**
 * Interface for working with item_order table in database
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public interface OrderDao {

	/**
	 * Add order to database
	 * 
	 * @param order order to add
	 * @return the added order
	 */
	Order create(Order order);

	/**
	 * Add information about ordered gift certificates to database
	 * 
	 * @param orderedGiftCertificates list of ordered gift certificates
	 */
	void createOrderDetails(List<OrderedGiftCertificate> orderedGiftCertificates);

	/**
	 * Find order in database by id
	 * 
	 * @param id the id of order to find
	 * @return the Optional of found order
	 */
	Optional<Order> findById(long id);

	/**
	 * Find orders in database by user id
	 * 
	 * @param userId     the user id to find orders
	 * @param pagination the information about pagination
	 * @return the list of found orders if orders are found, else emptyList
	 */
	List<Order> findByUserId(long userId, Pagination pagination);

	/**
	 * Get number of orders by user id in database
	 * 
	 * @param userId the user id to find orders
	 * @return the number of found orders
	 */
	long getTotalNumberByUserId(long userId);
}
