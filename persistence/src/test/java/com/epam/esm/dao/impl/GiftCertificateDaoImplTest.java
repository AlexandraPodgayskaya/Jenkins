package com.epam.esm.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.configuration.TestConfiguration;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.creator.GiftCertificateSearchParameters;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;

@SpringBootTest(classes = TestConfiguration.class)
@Transactional
public class GiftCertificateDaoImplTest {

	private static GiftCertificate giftCertificate;
	private static GiftCertificate giftCertificate2;
	private static GiftCertificate giftCertificate3;
	private static GiftCertificateSearchParameters searchParameters1;
	private static GiftCertificateSearchParameters searchParameters2;
	private final GiftCertificateDao giftCertificateDao;
	private static Pagination pagination;

	@Autowired
	public GiftCertificateDaoImplTest(GiftCertificateDao giftCertificateDao) {
		this.giftCertificateDao = giftCertificateDao;
	}

	@BeforeAll
	public static void setUp() {
		giftCertificate = new GiftCertificate(null, "Cinema", "Best cinema in the city", new BigDecimal(100), 5,
				LocalDateTime.of(2020, 12, 12, 12, 0, 0), LocalDateTime.of(2020, 12, 13, 12, 0, 0), Boolean.FALSE);
		giftCertificate2 = new GiftCertificate(1L,
				"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
				"You will like it", new BigDecimal(100), 10, LocalDateTime.of(2020, 12, 12, 12, 0, 0),
				LocalDateTime.of(2020, 12, 13, 12, 0, 0), Boolean.FALSE);
		giftCertificate3 = new GiftCertificate(3L, "Horse ride", "Horseback ride for lovers - a Hollywood-style date",
				new BigDecimal("500.00"), 2, LocalDateTime.of(2017, 05, 22, 12, 46, 31),
				LocalDateTime.of(2020, 03, 20, 16, 34, 49), Boolean.FALSE, Collections.emptyList());
		searchParameters1 = new GiftCertificateSearchParameters(null, "with");
		searchParameters2 = new GiftCertificateSearchParameters(Arrays.asList("travel"), "good");
		pagination = new Pagination(1, 5);

	}

	@AfterAll
	public static void tearDown() {
		giftCertificate = null;
		giftCertificate2 = null;
		giftCertificate3 = null;
		searchParameters1 = null;
		searchParameters2 = null;
		pagination = null;
	}

	@Test
	public void createPositiveTest() {
		GiftCertificate actual = giftCertificateDao.create(giftCertificate);
		assertNotNull(actual);
	}

	@Test
	public void createThrowInvalidDataAccessApiUsageExceptionTest() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> giftCertificateDao.create(giftCertificate2));
	}

	@Test
	public void findBySearchParametersPositiveTest() {
		final int expectedNumberGiftCertificates = 1;
		List<GiftCertificate> actual = giftCertificateDao.findBySearchParameters(pagination, searchParameters1);
		assertEquals(expectedNumberGiftCertificates, actual.size());
	}

	@Test
	public void findBySearchParametersNegativeTest() {
		List<GiftCertificate> actual = giftCertificateDao.findBySearchParameters(pagination, searchParameters2);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void findByIdPositiveTest() {
		final long id = 3;
		Optional<GiftCertificate> actual = giftCertificateDao.findById(id);
		assertEquals(Optional.of(giftCertificate3), actual);
	}

	@Test
	public void findByIdNegativeTest() {
		final long id = 10;
		Optional<GiftCertificate> actual = giftCertificateDao.findById(id);
		assertFalse(actual.isPresent());
	}

	@Test
	public void findByNamePositiveTest() {
		final String name = "Horse ride";
		Optional<GiftCertificate> actual = giftCertificateDao.findByName(name);
		assertEquals(Optional.of(giftCertificate3), actual);
	}

	@Test
	public void findByNameNegativeTest() {
		final String name = "Massage";
		Optional<GiftCertificate> actual = giftCertificateDao.findByName(name);
		assertFalse(actual.isPresent());
	}

	@Test
	public void updateThrowDataIntegrityViolationExceptionTest() {
		assertThrows(DataIntegrityViolationException.class, () -> giftCertificateDao.update(giftCertificate2));
	}

	@Test
	public void deletePositiveTest() {
		final long id = 2;
		assertTrue(giftCertificateDao.delete(id));
	}

	@Test
	public void deleteNegativeTest() {
		final long id = 0;
		assertFalse(giftCertificateDao.delete(id));
	}
}
