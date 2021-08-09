package com.epam.esm.service.imp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.esm.configuration.ServiceConfiguration;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.creator.GiftCertificateSearchParameters;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificateSearchParametersDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.GiftCertificateValidator;

@SpringBootTest(classes = ServiceConfiguration.class)
public class GiftCertificateServiceImplTest {

	@MockBean
	private GiftCertificateDao giftCertificateDao;
	@MockBean
	private GiftCertificateValidator giftCertificateValidator;
	@MockBean
	private TagService tagService;
	@Autowired
	private GiftCertificateService giftCertificateService;
	private static GiftCertificateDto giftCertificateDto1;
	private static GiftCertificateDto giftCertificateDto2;
	private static GiftCertificateDto giftCertificateDto3;
	private static GiftCertificateDto giftCertificateDto4;
	private static GiftCertificate giftCertificate1;
	private static GiftCertificate giftCertificate2;
	private static PaginationDto pagination;
	private static PageDto<GiftCertificateDto> page;

	@BeforeAll
	public static void setUp() {
		giftCertificateDto1 = new GiftCertificateDto(null, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0),
				Collections.emptyList());
		giftCertificateDto2 = new GiftCertificateDto(1L, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), null);
		giftCertificateDto3 = new GiftCertificateDto(1L, "", "", new BigDecimal(100), -5,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0),
				Collections.emptyList());
		giftCertificateDto4 = new GiftCertificateDto(1L, "Travel to German", "You will like it", new BigDecimal(100),
				10, LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), null);
		giftCertificate1 = new GiftCertificate(1L, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Boolean.FALSE);
		giftCertificate2 = new GiftCertificate(1L, "Travel to German", "You will like it", new BigDecimal(100), 10,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Boolean.FALSE);
		pagination = new PaginationDto(1, 5);
		page = new PageDto<>(Arrays.asList(giftCertificateDto2, giftCertificateDto4), 2L);
	}

	@AfterAll
	public static void tearDown() {
		giftCertificateDto1 = null;
		giftCertificateDto2 = null;
		giftCertificateDto3 = null;
		giftCertificateDto4 = null;
		giftCertificate1 = null;
		giftCertificate2 = null;
		pagination = null;
		page = null;
	}

	@Test
	public void createGiftCertificatePositiveTest() {
		doNothing().when(giftCertificateValidator).validate(isA(GiftCertificateDto.class));
		when(giftCertificateDao.create(isA(GiftCertificate.class))).thenReturn(giftCertificate1);
		GiftCertificateDto actual = giftCertificateService.createGiftCertificate(giftCertificateDto1);
		assertEquals(giftCertificateDto2, actual);
	}

	@Test
	public void createGiftCertificateThrowIncorrectParameterValueExceptionTest() {
		doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator)
				.validate(isA(GiftCertificateDto.class));
		assertThrows(IncorrectParameterValueException.class,
				() -> giftCertificateService.createGiftCertificate(giftCertificateDto3));
	}

	@Test
	public void findGiftCertificatesPositiveTest() {
		when(giftCertificateDao.findBySearchParameters(isA(Pagination.class),
				isA(GiftCertificateSearchParameters.class)))
						.thenReturn(Arrays.asList(giftCertificate1, giftCertificate2));
		when(giftCertificateDao.getTotalNumber(isA(GiftCertificateSearchParameters.class))).thenReturn(2L);
		PageDto<GiftCertificateDto> actualPage = giftCertificateService.findGiftCertificates(pagination,
				new GiftCertificateSearchParametersDto());
		assertEquals(page, actualPage);
	}

	@Test
	void findGiftCertificateByIdPositiveTest() {
		final long id = 1;
		doNothing().when(giftCertificateValidator).validateId(anyLong());
		when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificate1));
		GiftCertificateDto actual = giftCertificateService.findGiftCertificateById(id);
		assertEquals(giftCertificateDto2, actual);
	}

	@Test
	public void findGiftCertificateByIdThrowIncorrectParameterValueExceptionTest() {
		final long id = 0;
		doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.findGiftCertificateById(id));
	}

	@Test
	public void findGiftCertificateByIdThrowResourceNotFoundExceptionTest() {
		final long id = 5L;
		doNothing().when(giftCertificateValidator).validateId(anyLong());
		when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.findGiftCertificateById(id));
	}

	@Test
	public void updateGiftCertificatePositiveTest() {
		when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificate2));
		doNothing().when(giftCertificateValidator).validate(isA(GiftCertificateDto.class));
		when(giftCertificateDao.update(isA(GiftCertificate.class))).thenReturn(giftCertificate1);
		GiftCertificateDto actual = giftCertificateService.updateGiftCertificate(giftCertificateDto4);
		assertEquals(giftCertificateDto2, actual);
	}

	@Test
	public void updateGiftCertificateThrowIncorrectParameterValueExceptionTest() {
		when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificate2));
		doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator)
				.validate(isA(GiftCertificateDto.class));
		assertThrows(IncorrectParameterValueException.class,
				() -> giftCertificateService.updateGiftCertificate(giftCertificateDto3));
	}

	@Test
	public void updateGiftCertificateThrowResourceNotFoundExceptionTest() {
		when(giftCertificateDao.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
		assertThrows(ResourceNotFoundException.class,
				() -> giftCertificateService.updateGiftCertificate(giftCertificateDto2));
	}

	@Test
	public void deleteGiftCertificatePositiveTest() {
		final long id = 1;
		doNothing().when(giftCertificateValidator).validateId(anyLong());
		when(giftCertificateDao.delete(anyLong())).thenReturn(Boolean.TRUE);
		when(giftCertificateDao.deleteConnectionByGiftCertificateId(anyLong())).thenReturn(Boolean.TRUE);
		assertDoesNotThrow(() -> giftCertificateService.deleteGiftCertificate(id));
	}

	@Test
	public void deleteGiftCertificateThrowIncorrectParameterValueExceptionTest() {
		final long id = -5;
		doThrow(new IncorrectParameterValueException()).when(giftCertificateValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> giftCertificateService.deleteGiftCertificate(id));
	}

	@Test
	public void deleteGiftCertificateThrowResourceNotFoundExceptionTest() {
		final long id = 10;
		doNothing().when(giftCertificateValidator).validateId(anyLong());
		when(giftCertificateDao.delete(anyLong())).thenReturn(Boolean.FALSE);
		assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.deleteGiftCertificate(id));
	}
}
