package com.epam.esm.service.imp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.esm.configuration.ServiceConfiguration;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderedGiftCertificateDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderedGiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.UserValidator;

@SpringBootTest(classes = ServiceConfiguration.class)
public class OrderServiceImplTest {

	@MockBean
	private OrderDao orderDao;
	@MockBean
	private OrderValidator orderValidator;
	@MockBean
	private UserValidator userValidator;
	@MockBean
	private UserService userService;
	@MockBean
	private GiftCertificateService giftCertificateService;
	@Autowired
	private OrderService orderService;
	private static GiftCertificateDto giftCertificateDto1;
	private static OrderedGiftCertificateDto orderedGiftCertificateDto;
	private static OrderedGiftCertificateDto orderedGiftCertificateDto1;
	private static OrderedGiftCertificate orderedGiftCertificate;
	private static OrderDto orderDto;
	private static OrderDto orderDto1;
	private static OrderDto orderDto2;
	private static Order order1;
	private static PaginationDto pagination;
	private static PageDto<OrderDto> page;

	@BeforeAll
	public static void setUp() {
		giftCertificateDto1 = new GiftCertificateDto(null, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), null);
		orderedGiftCertificateDto = new OrderedGiftCertificateDto(new GiftCertificateDto(1L), 5);
		orderedGiftCertificateDto1 = new OrderedGiftCertificateDto(new GiftCertificateDto(1L), "Cinema",
				"Best cinema in the city", new BigDecimal(100), 5, LocalDateTime.of(2020, 12, 12, 12, 0, 0),
				LocalDateTime.of(2020, 12, 13, 12, 0, 0), 5);
		orderedGiftCertificate = new OrderedGiftCertificate(new Order(), new GiftCertificate(1L), "Cinema",
				"Best cinema in the city", new BigDecimal(100), 5, LocalDateTime.of(2020, 12, 12, 12, 0, 0),
				LocalDateTime.of(2020, 12, 13, 12, 0, 0), 5);
		orderDto = new OrderDto(1L, Arrays.asList(orderedGiftCertificateDto));
		orderDto1 = new OrderDto(1L, new BigDecimal(100), 1L, LocalDateTime.of(2020, 12, 12, 12, 0, 0),
				Arrays.asList(orderedGiftCertificateDto1));
		orderDto2 = new OrderDto(1L, Collections.emptyList());
		order1 = new Order(1L, new BigDecimal(100), 1L, LocalDateTime.of(2020, 12, 12, 12, 0, 0), Boolean.FALSE,
				Arrays.asList(orderedGiftCertificate));
		pagination = new PaginationDto(1, 5);
		page = new PageDto<>(Arrays.asList(orderDto1), 1L);
	}

	@AfterAll
	public static void tearDown() {
		giftCertificateDto1 = null;
		orderedGiftCertificateDto = null;
		orderedGiftCertificateDto1 = null;
		orderedGiftCertificate = null;
		orderDto = null;
		orderDto1 = null;
		orderDto2 = null;
		order1 = null;
		pagination = null;
		page = null;
	}

	@Test
	public void addOrderPositiveTest() {
		doNothing().when(orderValidator).validate(isA(OrderDto.class));
		when(giftCertificateService.findGiftCertificateById(anyLong())).thenReturn(giftCertificateDto1);
		when(orderDao.create(isA(Order.class))).thenReturn(order1);
		doNothing().when(orderDao).createOrderDetails(anyList());
		OrderDto actualOrder = orderService.addOrder(orderDto);
		assertEquals(orderDto1, actualOrder);
	}

	@Test
	public void addOrderThrowIncorrectParameterValueExceptionTest() {
		doThrow(new IncorrectParameterValueException()).when(orderValidator).validate(isA(OrderDto.class));
		assertThrows(IncorrectParameterValueException.class, () -> orderService.addOrder(orderDto2));
	}

	@Test
	public void findOrderByIdPositiveTest() {
		final long id = 1;
		doNothing().when(orderValidator).validateId(anyLong());
		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order1));
		OrderDto actual = orderService.findOrderById(id);
		assertEquals(orderDto1, actual);
	}

	@Test
	public void findOrderByIdThrowIncorrectParameterValueExceptionTest() {
		final long id = 0;
		doThrow(new IncorrectParameterValueException()).when(orderValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> orderService.findOrderById(id));
	}

	@Test
	public void findOrderByIdThrowResourceNotFoundExceptionTest() {
		final long id = 5;
		doNothing().when(orderValidator).validateId(anyLong());
		when(orderDao.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> orderService.findOrderById(id));
	}

	@Test
	public void findOrderByUserIdPositiveTest() {
		final long id = 1;
		doNothing().when(userValidator).validateId(anyLong());
		when(orderDao.findByUserId(anyLong(), isA(Pagination.class))).thenReturn(Arrays.asList(order1));
		when(orderDao.getTotalNumberByUserId(anyLong())).thenReturn(1L);
		PageDto<OrderDto> actualPage = orderService.findOrdersByUserId(id, pagination);
		assertEquals(page, actualPage);
	}

	@Test
	public void findOrderByUserIdThrowIncorrectParameterValueExceptionTest() {
		final long id = 0;
		doThrow(new IncorrectParameterValueException()).when(userValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> orderService.findOrdersByUserId(id, pagination));
	}
}
