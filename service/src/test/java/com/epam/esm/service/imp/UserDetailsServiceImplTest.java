package com.epam.esm.service.imp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.epam.esm.configuration.ServiceConfiguration;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.JwtUser;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;

@SpringBootTest(classes = ServiceConfiguration.class)
public class UserDetailsServiceImplTest {

	@MockBean
	private UserDao userDao;
	private static User user;
	private static JwtUser jwtUser;
	@Autowired
	private UserDetailsService userDetailsService;

	@BeforeAll
	public static void setUp() {
		user = new User(3L, "Ann", "email@tut.by", "password", Role.USER, Boolean.FALSE);
		jwtUser = new JwtUser(3L, "Ann", "email@tut.by", "password", Role.USER);
	}

	@AfterAll
	public static void tearDown() {
		user = null;
		jwtUser = null;
	}

	@Test
	public void loadUserByUsernamePositiveTest() {
		final String email = "email@tut.by";
		when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
		UserDetails actual = userDetailsService.loadUserByUsername(email);
		assertEquals(jwtUser, actual);
	}

	@Test
	public void loadUserByUsernameThrowUsernameNotFoundExceptionTest() {
		final String email = "someEmail@tut.by";
		when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
	}
}
