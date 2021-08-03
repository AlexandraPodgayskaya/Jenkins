package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.AccessVerifier;
import com.epam.esm.service.UserService;

/**
 * Class is an endpoint of the API which allows to perform operations on user
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {
	private static final String ORDERS = "orders";
	private final UserService userService;
	private final ParametersToDtoConverter parametersToDtoConverter;
	private final AccessVerifier accessVerifier;

	@Autowired
	public UserController(UserService userService, ParametersToDtoConverter parametersToDtoConverter,
			AccessVerifier accessVerifier) {
		this.userService = userService;
		this.parametersToDtoConverter = parametersToDtoConverter;
		this.accessVerifier = accessVerifier;
	}

	/**
	 * Get all users, processes GET requests at /users
	 * 
	 * @param pageParameters the information for pagination
	 * @return the page with found users and total number of positions
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('authority:read_all')")
	public PageDto<UserDto> getUsers(@RequestParam Map<String, String> pageParameters) {
		PaginationDto pagination = parametersToDtoConverter.getPaginationDto(pageParameters);
		PageDto<UserDto> page = userService.findAllUsers(pagination);
		page.getPagePositions().forEach(this::addLinks);
		return page;
	}

	/**
	 * Get user by id, processes GET requests at /users/{id}
	 * 
	 * @param id the user id which will be found
	 * @return the found user dto
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('authority:read')")
	public UserDto getUserById(@PathVariable long id) {
		accessVerifier.checkAccess(id);
		UserDto user = userService.findUserById(id);
		addLinks(user);
		return user;
	}

	private void addLinks(UserDto userDto) {
		userDto.add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel());
		userDto.add(linkTo(methodOn(OrderController.class).getOrdersByUserId(userDto.getId(), Collections.emptyMap()))
				.withRel(ORDERS));
	}
}
