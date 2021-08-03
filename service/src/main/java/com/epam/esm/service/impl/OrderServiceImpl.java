package com.epam.esm.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderedGiftCertificateDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderedGiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.UserValidator;

/**
 * Class is implementation of interface {@link OrderService} and intended to
 * work with order
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderDao orderDao;
	private final OrderValidator orderValidator;
	private final UserValidator userValidator;
	private final ModelMapper modelMapper;
	private final UserService userService;
	private final GiftCertificateService giftCertificateService;

	@Autowired
	public OrderServiceImpl(OrderDao orderDao, OrderValidator orderValidator, UserValidator userValidator,
			ModelMapper modelMapper, UserService userService, GiftCertificateService giftCertificateService) {
		this.orderDao = orderDao;
		this.orderValidator = orderValidator;
		this.userValidator = userValidator;
		this.modelMapper = modelMapper;
		this.userService = userService;
		this.giftCertificateService = giftCertificateService;
	}

	@Transactional
	@Override
	public OrderDto addOrder(OrderDto orderDto) throws IncorrectParameterValueException {
		orderValidator.validate(orderDto);
		try {
			userService.findUserById(orderDto.getUserId());
			orderDto.getOrderedGiftCertificates().forEach(this::setOrderedGiftCertificateDetails);
		} catch (ResourceNotFoundException exception) {
			Map<String, String> incorrectParameter = new HashMap<>();
			incorrectParameter.put(exception.getMessageKey(), exception.getMessageParameter());
			throw new IncorrectParameterValueException("resource not found", incorrectParameter,
					ErrorCode.ORDER.getCode(), exception);
		}
		Order order = modelMapper.map(orderDto, Order.class);
		setTotalCostOrder(order);
		Order addedOrder = orderDao.create(order);
		order.getOrderedGiftCertificates().forEach(giftCertificate -> giftCertificate.setOrder(addedOrder));
		orderDao.createOrderDetails(order.getOrderedGiftCertificates());
		OrderDto createdOrderDto = modelMapper.map(addedOrder, OrderDto.class);
		return createdOrderDto;
	}

	@Override
	public OrderDto findOrderById(long id) throws IncorrectParameterValueException, ResourceNotFoundException {
		orderValidator.validateId(id);
		return orderDao.findById(id).map(order -> modelMapper.map(order, OrderDto.class))
				.orElseThrow(() -> new ResourceNotFoundException("no order by id", MessageKey.ORDER_NOT_FOUND_BY_ID,
						String.valueOf(id), ErrorCode.ORDER.getCode()));
	}

	@Override
	public PageDto<OrderDto> findOrdersByUserId(long userId, PaginationDto paginationDto)
			throws IncorrectParameterValueException {
		userValidator.validateId(userId);
		Pagination pagination = modelMapper.map(paginationDto, Pagination.class);
		List<Order> foundOrders = orderDao.findByUserId(userId, pagination);
		List<OrderDto> foundOrdersDto = foundOrders.stream()
				.map(foundOrder -> modelMapper.map(foundOrder, OrderDto.class)).collect(Collectors.toList());
		long totalNumberPositions = orderDao.getTotalNumberByUserId(userId);
		PageDto<OrderDto> ordersPage = new PageDto<>(foundOrdersDto, totalNumberPositions);
		return ordersPage;
	}

	private void setOrderedGiftCertificateDetails(OrderedGiftCertificateDto orderedGiftCertificate) {
		GiftCertificateDto giftCertificate = giftCertificateService
				.findGiftCertificateById(orderedGiftCertificate.getGiftCertificate().getId());
		orderedGiftCertificate.setName(giftCertificate.getName());
		orderedGiftCertificate.setDescription(giftCertificate.getDescription());
		orderedGiftCertificate.setPrice(giftCertificate.getPrice());
		orderedGiftCertificate.setDuration(giftCertificate.getDuration());
		orderedGiftCertificate.setCreateDate(giftCertificate.getCreateDate());
		orderedGiftCertificate.setLastUpdateDate(giftCertificate.getLastUpdateDate());
	}

	private void setTotalCostOrder(Order order) {
		BigDecimal orderCost = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		for (OrderedGiftCertificate giftCertificate : order.getOrderedGiftCertificates()) {
			BigDecimal giftCertificatesCost = giftCertificate.getPrice()
					.multiply(BigDecimal.valueOf(giftCertificate.getNumber()));
			orderCost = orderCost.add(giftCertificatesCost);
		}
		order.setCost(orderCost);
	}
}
