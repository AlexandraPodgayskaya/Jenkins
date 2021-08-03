package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;

/**
 * Class is an endpoint of the API which allows to perform CRD operations on tag
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@RestController
@RequestMapping("/tags")
public class TagController {

	private static final String DELETE = "delete";
	private final TagService tagService;
	private final ParametersToDtoConverter parametersToDtoConverter;

	@Autowired
	public TagController(TagService tagService, ParametersToDtoConverter parametersToDtoConverter) {
		this.tagService = tagService;
		this.parametersToDtoConverter = parametersToDtoConverter;
	}

	/**
	 * Get all tags, processes GET requests at /tags
	 * 
	 * @param pageParameters the information for pagination
	 * @return the page with found tags and total number of positions
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('authority:read')")
	public PageDto<TagDto> getTags(@RequestParam Map<String, String> pageParameters) {
		PaginationDto pagination = parametersToDtoConverter.getPaginationDto(pageParameters);
		PageDto<TagDto> page = tagService.findAllTags(pagination);
		page.getPagePositions().forEach(this::addLinks);
		return page;
	}

	/**
	 * Get tag by id, processes GET requests at /tags/{id}
	 * 
	 * @param id the tag id which will be found
	 * @return the found tag dto
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('authority:read')")
	public TagDto getTagById(@PathVariable long id) {
		TagDto foundTagDto = tagService.findTagById(id);
		addLinks(foundTagDto);
		return foundTagDto;
	}

	/**
	 * Get the most popular tag of user with highest cost of all orders, processes
	 * GET requests at /tags/popular
	 * 
	 * @return the found tag dto
	 */
	@GetMapping("/popular")
	@PreAuthorize("hasAuthority('authority:read')")
	public TagDto getMostPopularTagOfUserWithHighestCostOfAllOrders() {
		TagDto foundTagDto = tagService.findMostPopularTagOfUserWithHighestCostOfAllOrders();
		addLinks(foundTagDto);
		return foundTagDto;
	}

	/**
	 * Add new tag, processes POST requests at /tags
	 * 
	 * @param tagDto the new tag which will be added
	 * @return the new tag dto
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('authority:write')")
	public TagDto addTag(@RequestBody TagDto tagDto) {
		TagDto addedTagDto = tagService.createTag(tagDto);
		addLinks(addedTagDto);
		return addedTagDto;
	}

	/**
	 * Delete tag by id, processes DELETE requests at /tags/{id}
	 * 
	 * @param id he tag id which will be deleted
	 * @return void
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('authority:write')")
	public ResponseEntity<Void> deleteTag(@PathVariable long id) {
		tagService.deleteTag(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private void addLinks(TagDto tagDto) {
		tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
		tagDto.add(linkTo(methodOn(TagController.class).deleteTag(tagDto.getId())).withRel(DELETE));
	}
}
