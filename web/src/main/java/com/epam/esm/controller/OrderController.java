package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.security.AccessVerifier;
import com.epam.esm.service.OrderService;

/**
 * Class is an endpoint of the API which allows to perform operations on order
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

	private static final String USER = "user";
	private static final String GIFT_CERTIFICATE = "gift-certificate";
	private final OrderService orderService;
	private final ParametersToDtoConverter parametersToDtoConverter;
	private final AccessVerifier accessVerifier;

	@Autowired
	public OrderController(OrderService orderService, ParametersToDtoConverter parametersToDtoConverter,
			AccessVerifier accessVerifier) {
		this.orderService = orderService;
		this.parametersToDtoConverter = parametersToDtoConverter;
		this.accessVerifier = accessVerifier;
	}

	/**
	 * Get order by id, processes GET requests at /orders/{id}
	 * 
	 * @param id the order id which will be found
	 * @return the found order dto
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('authority:read')")
	public OrderDto getOrderById(@PathVariable long id) {
		OrderDto foundOrderDto = orderService.findOrderById(id);
		accessVerifier.checkAccess(foundOrderDto.getUserId());
		addLinks(foundOrderDto);
		return foundOrderDto;
	}

	/**
	 * Get order by user id, processes GET requests at /orders/users/{userId}
	 * 
	 * @param userId         the user id
	 * @param pageParameters the information for pagination
	 * @return the page with found orders and total number of positions
	 */
	@GetMapping("/users/{userId}")
	@PreAuthorize("hasAuthority('authority:read')")
	public PageDto<OrderDto> getOrdersByUserId(@PathVariable long userId,
			@RequestParam Map<String, String> pageParameters) {
		accessVerifier.checkAccess(userId);
		PaginationDto pagination = parametersToDtoConverter.getPaginationDto(pageParameters);
		PageDto<OrderDto> page = orderService.findOrdersByUserId(userId, pagination);
		page.getPagePositions().forEach(this::addLinks);
		return page;
	}

	/**
	 * Add new order, processes POST requests at /orders
	 * 
	 * @param orderDto the new order which will be added
	 * @return the added order dto
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('authority:make_order')")
	public OrderDto addOrder(@RequestBody OrderDto orderDto) {
		accessVerifier.checkAccess(orderDto.getUserId());
		OrderDto addedOrderDto = orderService.addOrder(orderDto);
		addLinks(addedOrderDto);
		return addedOrderDto;
	}

	private void addLinks(OrderDto orderDto) {
		orderDto.add(linkTo(methodOn(OrderController.class).getOrderById(orderDto.getId())).withSelfRel());
		orderDto.add(linkTo(methodOn(UserController.class).getUserById(orderDto.getUserId())).withRel(USER));
		orderDto.getOrderedGiftCertificates().forEach(
				orderedGiftCertificate -> orderedGiftCertificate.add(linkTo(methodOn(GiftCertificateController.class)
						.getGiftCertificateById(orderedGiftCertificate.getGiftCertificate().getId()))
								.withRel(GIFT_CERTIFICATE)));
	}

}
