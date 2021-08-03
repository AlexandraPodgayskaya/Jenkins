package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.User;

/**
 * Interface for working with user table in database
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public interface UserDao {

	/**
	 * Add user to database
	 * 
	 * @param user user to add
	 * @return the added user
	 */
	User create(User user);

	/**
	 * Find all users in database
	 * 
	 * @param pagination the information about pagination
	 * @return the list of found users if users are found, else emptyList
	 */
	List<User> findAll(Pagination pagination);

	/**
	 * Find user in database by id
	 * 
	 * @param id the id of user to find
	 * @return the Optional of found user
	 */
	Optional<User> findById(long id);

	/**
	 * Find user in database by email
	 * 
	 * @param email the email of user to find
	 * @return the Optional of found user
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Get number of users in database
	 * 
	 * @return the number of found users
	 */
	public long getTotalNumber();

}
