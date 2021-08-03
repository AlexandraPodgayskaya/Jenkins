package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;

public interface OrderService {

	/**
	 * Add order to database
	 * 
	 * @param orderDto order to add
	 * @return the added order
	 */
	OrderDto addOrder(OrderDto orderDto);

	/**
	 * Find order in database by id
	 * 
	 * @param id the id of order to find
	 * @return the Optional of found order
	 */
	OrderDto findOrderById(long id);

	/**
	 * Find orders by user id
	 *
	 * @param userId     the user id to find order
	 * @param pagination the information about pagination
	 * @return the page with found orders and total number of positions
	 */
	PageDto<OrderDto> findOrdersByUserId(long userId, PaginationDto pagination);

}
