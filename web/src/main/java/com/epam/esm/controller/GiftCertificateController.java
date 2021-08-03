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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.converter.ParametersToDtoConverter;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateSearchParametersDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.service.GiftCertificateService;

/**
 * Class is an endpoint of the API which allows to perform CRUD operations on
 * gift certificates
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	private final GiftCertificateService giftCertificateService;
	private final ParametersToDtoConverter parametersToDtoConverter;

	@Autowired
	public GiftCertificateController(GiftCertificateService giftCertificateService,
			ParametersToDtoConverter parametersToDtoConverter) {
		this.giftCertificateService = giftCertificateService;
		this.parametersToDtoConverter = parametersToDtoConverter;
	}

	/**
	 * Get gift certificates by parameters, processes GET requests at
	 * /gift-certificates
	 * 
	 * @param parameters the gift certificate search parameters and information for
	 *                   pagination
	 * @return the page with found gift certificates and total number of positions
	 */
	@GetMapping
	public PageDto<GiftCertificateDto> getGiftCertificates(@RequestParam Map<String, String> parameters) {
		PaginationDto pagination = parametersToDtoConverter.getPaginationDto(parameters);
		GiftCertificateSearchParametersDto giftCertificateSearchParametersDto = parametersToDtoConverter
				.getGiftCertificateSearchParametersDto(parameters);
		PageDto<GiftCertificateDto> page = giftCertificateService.findGiftCertificates(pagination,
				giftCertificateSearchParametersDto);
		page.getPagePositions().forEach(this::addLinks);
		return page;
	}

	/**
	 * Get gift certificate by id, processes GET requests at /gift-certificates/{id}
	 * 
	 * @param id the gift certificate id which will be found
	 * @return the found gift certificate dto
	 */
	@GetMapping("/{id}")
	public GiftCertificateDto getGiftCertificateById(@PathVariable long id) {
		GiftCertificateDto foundGiftCertificateDto = giftCertificateService.findGiftCertificateById(id);
		addLinks(foundGiftCertificateDto);
		return foundGiftCertificateDto;
	}

	/**
	 * Add new gift certificate, processes POST requests at /gift-certificates
	 * 
	 * @param giftCertificateDto the new gift certificate which will be added
	 * @return the added gift certificate dto
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('authority:write')")
	public GiftCertificateDto createGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
		GiftCertificateDto createdGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificateDto);
		addLinks(createdGiftCertificate);
		return createdGiftCertificate;
	}

	/**
	 * Update gift certificate, processes PATCH requests at /gift-certificates/{id}
	 * 
	 * @param id                 the gift certificate id which will be updated
	 * @param giftCertificateDto the gift certificate dto with updated fields
	 * @return the updated gift certificate dto
	 */
	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('authority:write')")
	public GiftCertificateDto updateGiftCertificate(@PathVariable long id,
			@RequestBody GiftCertificateDto giftCertificateDto) {
		giftCertificateDto.setId(id);
		GiftCertificateDto updatedGiftCertificate = giftCertificateService.updateGiftCertificate(giftCertificateDto);
		addLinks(updatedGiftCertificate);
		return updatedGiftCertificate;
	}

	/**
	 * Delete gift certificate by id, processes DELETE requests at
	 * /gift-certificates/{id}
	 * 
	 * @param id the gift certificate id which will be deleted
	 * @return void
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('authority:write')")
	public ResponseEntity<Void> deleteGiftCertificate(@PathVariable long id) {
		giftCertificateService.deleteGiftCertificate(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private void addLinks(GiftCertificateDto giftCertificateDto) {
		giftCertificateDto.add(
				linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(giftCertificateDto.getId()))
						.withSelfRel());
		giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
				.updateGiftCertificate(giftCertificateDto.getId(), giftCertificateDto)).withRel(UPDATE));
		giftCertificateDto
				.add(linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificate(giftCertificateDto.getId()))
						.withRel(DELETE));
		giftCertificateDto.getTags().forEach(
				tagDto -> tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel()));
	}
}
