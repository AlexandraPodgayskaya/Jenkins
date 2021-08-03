package com.epam.esm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.creator.GiftCertificateSearchParameters;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateSearchParametersDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageKey;
import com.epam.esm.validator.GiftCertificateValidator;

/**
 * Class is implementation of interface {@link GiftCertificateService} and
 * intended to work with gift certificate
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

	private final GiftCertificateDao giftCertificateDao;
	private final GiftCertificateValidator giftCertificateValidator;
	private final TagService tagService;
	private final ModelMapper modelMapper;

	@Autowired
	public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
			GiftCertificateValidator giftCertificateValidator, TagService tagService, ModelMapper modelMapper) {
		this.giftCertificateDao = giftCertificateDao;
		this.giftCertificateValidator = giftCertificateValidator;
		this.tagService = tagService;
		this.modelMapper = modelMapper;
	}

	@Transactional
	@Override
	public GiftCertificateDto createGiftCertificate(GiftCertificateDto giftCertificateDto)
			throws IncorrectParameterValueException {
		giftCertificateValidator.validate(giftCertificateDto);
		checkUniquenessName(giftCertificateDto.getName());
		giftCertificateDto.setTags(createTags(giftCertificateDto.getTags()));
		GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
		GiftCertificate createdGiftCertificate = giftCertificateDao.create(giftCertificate);
		return modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
	}

	@Override
	public PageDto<GiftCertificateDto> findGiftCertificates(PaginationDto paginationDto,
			GiftCertificateSearchParametersDto searchParametersDto) {
		Pagination pagination = modelMapper.map(paginationDto, Pagination.class);
		GiftCertificateSearchParameters searchParameters = modelMapper.map(searchParametersDto,
				GiftCertificateSearchParameters.class);
		List<GiftCertificate> foundGiftCertificates = giftCertificateDao.findBySearchParameters(pagination,
				searchParameters);
		List<GiftCertificateDto> foundGiftCertificatesDto = foundGiftCertificates.stream()
				.map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
				.collect(Collectors.toList());
		long totalNumberPositions = giftCertificateDao.getTotalNumber(searchParameters);
		PageDto<GiftCertificateDto> page = new PageDto<>(foundGiftCertificatesDto, totalNumberPositions);
		return page;
	}

	@Override
	public GiftCertificateDto findGiftCertificateById(long id)
			throws IncorrectParameterValueException, ResourceNotFoundException {
		giftCertificateValidator.validateId(id);
		Optional<GiftCertificate> foundGiftCertificateOptional = giftCertificateDao.findById(id);
		return foundGiftCertificateOptional
				.map(foundGiftCertificate -> modelMapper.map(foundGiftCertificate, GiftCertificateDto.class))
				.orElseThrow(() -> new ResourceNotFoundException("no gift certificate by id",
						MessageKey.GIFT_CERTIFICATE_NOT_FOUND_BY_ID, String.valueOf(id),
						ErrorCode.GIFT_CERTIFICATE.getCode()));
	}

	@Transactional
	@Override
	public GiftCertificateDto updateGiftCertificate(GiftCertificateDto giftCertificateDto) {
		GiftCertificateDto foundGiftCertificateDto = findGiftCertificateById(giftCertificateDto.getId());
		giftCertificateDto.setTags(createTags(giftCertificateDto.getTags()));
		String giftCertificateName = giftCertificateDto.getName();
		if (giftCertificateName != null && !giftCertificateName.equals(foundGiftCertificateDto.getName())) {
			checkUniquenessName(giftCertificateDto.getName());
		}
		updateFields(foundGiftCertificateDto, giftCertificateDto);
		giftCertificateValidator.validate(foundGiftCertificateDto);
		GiftCertificate updatedGiftCertificate = giftCertificateDao
				.update(modelMapper.map(foundGiftCertificateDto, GiftCertificate.class));
		return modelMapper.map(updatedGiftCertificate, GiftCertificateDto.class);
	}

	@Transactional
	@Override
	public void deleteGiftCertificate(long id) throws IncorrectParameterValueException, ResourceNotFoundException {
		giftCertificateValidator.validateId(id);
		if (!giftCertificateDao.delete(id)) {
			throw new ResourceNotFoundException("no gift certificate to remove by id",
					MessageKey.GIFT_CERTIFICATE_NOT_FOUND_BY_ID, String.valueOf(id),
					ErrorCode.GIFT_CERTIFICATE.getCode());
		}
		giftCertificateDao.deleteConnectionByGiftCertificateId(id);
	}

	private void checkUniquenessName(String name) {
		Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findByName(name);
		if (giftCertificateOptional.isPresent()) {
			Map<String, String> incorrectParameter = new HashMap<>();
			incorrectParameter.put(MessageKey.PARAMETER_REPEATED_NAME, name);
			throw new IncorrectParameterValueException("repeated gift certificate name", incorrectParameter,
					ErrorCode.GIFT_CERTIFICATE.getCode());
		}
	}

	private List<TagDto> createTags(List<TagDto> tagsToCreate) {
		if (tagsToCreate != null) {
			tagsToCreate = tagsToCreate.stream().distinct().map(tagService::createTag).collect(Collectors.toList());
		}
		return tagsToCreate;
	}

	private void updateFields(GiftCertificateDto foundGiftCertificate, GiftCertificateDto receivedGiftCertificate) {
		int fieldCounter = 0;
		if (Objects.nonNull(receivedGiftCertificate.getName())) {
			foundGiftCertificate.setName(receivedGiftCertificate.getName());
			fieldCounter++;
		}
		if (Objects.nonNull(receivedGiftCertificate.getDescription())) {
			foundGiftCertificate.setDescription(receivedGiftCertificate.getDescription());
			fieldCounter++;
		}
		if (Objects.nonNull(receivedGiftCertificate.getPrice())) {
			foundGiftCertificate.setPrice(receivedGiftCertificate.getPrice());
			fieldCounter++;
		}
		if (Objects.nonNull(receivedGiftCertificate.getDuration())) {
			foundGiftCertificate.setDuration(receivedGiftCertificate.getDuration());
			fieldCounter++;
		}
		if (Objects.nonNull(receivedGiftCertificate.getTags())) {
			foundGiftCertificate.setTags(receivedGiftCertificate.getTags());
			fieldCounter++;
		}
		if (fieldCounter == 0) {
			Map<String, String> incorrectParameter = new HashMap<>();
			incorrectParameter.put(MessageKey.NO_FIELDS_TO_UPDATE, StringUtils.EMPTY);
			throw new IncorrectParameterValueException("no fields to update", incorrectParameter,
					ErrorCode.GIFT_CERTIFICATE.getCode());
		}
	}
}
