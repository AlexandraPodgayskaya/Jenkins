package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;

/**
 * Interface for working with tag table in database
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public interface TagDao {

	/**
	 * Add tag to database
	 * 
	 * @param tag tag to add
	 * @return the added tag
	 */
	Tag create(Tag tag);

	/**
	 * Find all tags in database
	 * 
	 * @param pagination the information about pagination
	 * @return the list of found tags if tags are found, else emptyList
	 */
	List<Tag> findAll(Pagination pagination);

	/**
	 * Find tag in database by id
	 * 
	 * @param id - the id of tag to find
	 * @return the Optional of found tag
	 */
	Optional<Tag> findById(long id);

	/**
	 * Find tag in database by name
	 * 
	 * @param tagName - the name of tag to find
	 * @return the Optional of found tag
	 */
	Optional<Tag> findByName(String tagName);

	/**
	 * Find most popular tag of user with highest cost of all orders in database
	 * 
	 * @return the Optional of found tag
	 */
	Optional<Tag> findMostPopularTagOfUserWithHighestCostOfAllOrders();

	/**
	 * Remove tag from database
	 * 
	 * @param id - the id of tag to remove
	 * @return boolean true if everything go correct, else false
	 */

	boolean delete(long id);

	/**
	 * Remove gift certificate and tag connection from database
	 * 
	 * @param id the id of tag to remove connection
	 * @return boolean true if everything go correct, else false
	 */
	boolean deleteConnectionByTagId(long id);

	/**
	 * Get number of tags in database
	 * 
	 * @return the number of found tags
	 */
	long getTotalNumber();

}
