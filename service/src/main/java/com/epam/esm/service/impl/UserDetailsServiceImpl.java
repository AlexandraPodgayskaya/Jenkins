package com.epam.esm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.JwtUser;
import com.epam.esm.entity.User;

/**
 * Class is implementation of interface UserDetailsService and loads user data
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserDao userDao;

	@Autowired
	public UserDetailsServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> foundUser = userDao.findByEmail(email);
		if (foundUser.isEmpty()) {
			throw new UsernameNotFoundException("user doesn't exists");
		}
		User user = foundUser.get();
		return new JwtUser(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
	}
}
