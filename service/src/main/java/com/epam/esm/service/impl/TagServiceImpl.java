package com.epam.esm.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.TagValidator;

/**
 * Class is implementation of interface {@link TagService} and intended to work
 * with gift certificate
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Service
public class TagServiceImpl implements TagService {

	private final TagDao tagDao;
	private final TagValidator tagValidator;
	private final ModelMapper modelMapper;

	@Autowired
	public TagServiceImpl(TagDao tagDao, TagValidator tagValidator, ModelMapper modelMapper) {
		this.tagDao = tagDao;
		this.tagValidator = tagValidator;
		this.modelMapper = modelMapper;
	}

	@Transactional
	@Override
	public TagDto createTag(TagDto tagDto) throws IncorrectParameterValueException {
		tagValidator.validateName(tagDto.getName());
		Optional<Tag> tagOptional = tagDao.findByName(tagDto.getName());
		Tag createdTag = tagOptional.orElseGet(() -> tagDao.create(modelMapper.map(tagDto, Tag.class)));
		return modelMapper.map(createdTag, TagDto.class);
	}

	@Override
	public PageDto<TagDto> findAllTags(PaginationDto paginationDto) {
		Pagination pagination = modelMapper.map(paginationDto, Pagination.class);
		List<Tag> foundTags = tagDao.findAll(pagination);
		List<TagDto> foundTagsDto = foundTags.stream().map(foundTag -> modelMapper.map(foundTag, TagDto.class))
				.collect(Collectors.toList());
		long totalNumberPositions = tagDao.getTotalNumber();
		PageDto<TagDto> tagsPage = new PageDto<>(foundTagsDto, totalNumberPositions);
		return tagsPage;
	}

	@Override
	public TagDto findTagById(long id) throws IncorrectParameterValueException, ResourceNotFoundException {
		tagValidator.validateId(id);
		Optional<Tag> foundTag = tagDao.findById(id);
		return foundTag.map(tag -> modelMapper.map(tag, TagDto.class))
				.orElseThrow(() -> new ResourceNotFoundException("no tag by id", MessageKey.TAG_NOT_FOUND_BY_ID,
						String.valueOf(id), ErrorCode.TAG.getCode()));
	}

	@Override
	public TagDto findMostPopularTagOfUserWithHighestCostOfAllOrders() {
		Optional<Tag> foundTagOptional = tagDao.findMostPopularTagOfUserWithHighestCostOfAllOrders();
		return foundTagOptional.map(foundTag -> modelMapper.map(foundTag, TagDto.class))
				.orElseThrow(() -> new ResourceNotFoundException(
						"most popular tag of user with highest cost of all orders not found", MessageKey.TAG_NOT_FOUND,
						StringUtils.EMPTY, ErrorCode.TAG.getCode()));
	}

	@Transactional
	@Override
	public void deleteTag(long id) throws IncorrectParameterValueException, ResourceNotFoundException {
		tagValidator.validateId(id);
		if (!tagDao.delete(id)) {
			throw new ResourceNotFoundException("no tag to remove by id", MessageKey.TAG_NOT_FOUND_BY_ID,
					String.valueOf(id), ErrorCode.TAG.getCode());
		}
		tagDao.deleteConnectionByTagId(id);
	}
}
