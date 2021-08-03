package com.epam.esm.service.imp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.esm.configuration.ServiceConfiguration;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserValidator;

@SpringBootTest(classes = ServiceConfiguration.class)
public class UserServiceImplTest {

	@MockBean
	private UserDao userDao;
	@MockBean
	private UserValidator userValidator;
	@Autowired
	private UserService userService;
	private static UserDto userDto1;
	private static UserDto userDto2;
	private static UserDto userDto3;
	private static UserDto userDto4;
	private static User user1;
	private static User user2;
	private static User user3;
	private static PaginationDto pagination;
	private static PageDto<UserDto> page;

	@BeforeAll
	public static void setUp() {
		userDto1 = new UserDto(1L, "Alex");
		userDto2 = new UserDto(2L, "Tim");
		userDto3 = new UserDto("Ann", "email@tut.by", "password");
		userDto4 = new UserDto(3L, "Ann", "email@tut.by", "password", Role.USER);
		user1 = new User(1L, "Alex", Boolean.FALSE);
		user2 = new User(2L, "Tim", Boolean.FALSE);
		user3 = new User(3L, "Ann", "email@tut.by", "password", Role.USER, Boolean.FALSE);
		pagination = new PaginationDto(1, 5);
		page = new PageDto<>(Arrays.asList(userDto1, userDto2), 2L);
	}

	@AfterAll
	public static void tearDown() {
		userDto1 = null;
		userDto2 = null;
		userDto3 = null;
		userDto4 = null;
		user1 = null;
		user2 = null;
		user3 = null;
		pagination = null;
		page = null;
	}

	@Test
	public void createUserPositiveTest() {
		doNothing().when(userValidator).validateUser(isA(UserDto.class));
		when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
		when(userDao.create(isA(User.class))).thenReturn(user3);
		UserDto actual = userService.createUser(userDto3);
		assertEquals(userDto4, actual);
	}

	@Test
	public void createUserThrowIncorrectParameterValueExceptionTest() {
		doThrow(new IncorrectParameterValueException()).when(userValidator).validateUser(isA(UserDto.class));
		assertThrows(IncorrectParameterValueException.class, () -> userService.createUser(userDto3));
	}

	@Test
	public void findAllUsersPositiveTest() {
		when(userDao.findAll(isA(Pagination.class))).thenReturn(Arrays.asList(user1, user2));
		when(userDao.getTotalNumber()).thenReturn(2L);
		PageDto<UserDto> actualPage = userService.findAllUsers(pagination);
		assertEquals(page, actualPage);
	}

	@Test
	public void findUserByIdPositiveTest() {
		final long id = 1;
		doNothing().when(userValidator).validateId(anyLong());
		when(userDao.findById(anyLong())).thenReturn(Optional.of(user1));
		UserDto actual = userService.findUserById(id);
		assertEquals(userDto1, actual);
	}

	@Test
	public void findUserByIdThrowIncorrectParameterValueExceptionTest() {
		final long id = 0;
		doThrow(new IncorrectParameterValueException()).when(userValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> userService.findUserById(id));
	}

	@Test
	public void findUserByIdThrowResourceNotFoundExceptionTest() {
		final long id = 5;
		doNothing().when(userValidator).validateId(anyLong());
		when(userDao.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(id));
	}
}
