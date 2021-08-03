package com.epam.esm.service.imp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.esm.configuration.ServiceConfiguration;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;

@SpringBootTest(classes = ServiceConfiguration.class)
public class TagServiceImplTest {

	@MockBean
	private TagDao tagDao;
	@MockBean
	private TagValidator tagValidator;
	@Autowired
	private TagService tagService;
	private static TagDto tagDto1;
	private static TagDto tagDto2;
	private static TagDto tagDto3;
	private static TagDto tagDto4;
	private static Tag tag1;
	private static Tag tag2;
	private static PaginationDto pagination;
	private static PageDto<TagDto> page;

	@BeforeAll
	public static void setUp() {
		tagDto1 = new TagDto("travel");
		tagDto2 = new TagDto(1L, "travel");
		tagDto3 = new TagDto("");
		tagDto4 = new TagDto(2L, "gift");
		tag1 = new Tag(1L, "travel", Boolean.FALSE);
		tag2 = new Tag(2L, "gift", Boolean.FALSE);
		pagination = new PaginationDto(1, 5);
		page = new PageDto<>(Arrays.asList(tagDto2, tagDto4), 2L);
	}

	@AfterAll
	public static void tearDown() {
		tagDto1 = null;
		tagDto1 = null;
		tagDto3 = null;
		tagDto4 = null;
		tag1 = null;
		tag2 = null;
		pagination = null;
		page = null;
	}

	@Test
	public void createTagPositiveTest() {
		doNothing().when(tagValidator).validateName(anyString());
		when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
		when(tagDao.create(isA(Tag.class))).thenReturn(tag1);
		TagDto actual = tagService.createTag(tagDto1);
		assertEquals(tagDto2, actual);
	}

	@Test
	public void createTagThrowIncorrectParameterValueExceptionTest() {
		doThrow(new IncorrectParameterValueException()).when(tagValidator).validateName(anyString());
		assertThrows(IncorrectParameterValueException.class, () -> tagService.createTag(tagDto3));
	}

	@Test
	public void findAllTagsPositiveTest() {
		when(tagDao.findAll(isA(Pagination.class))).thenReturn(Arrays.asList(tag1, tag2));
		when(tagDao.getTotalNumber()).thenReturn(2L);
		PageDto<TagDto> actualPage = tagService.findAllTags(pagination);
		assertEquals(page, actualPage);
	}

	@Test
	public void findTagByIdPositiveTest() {
		final long id = 1;
		doNothing().when(tagValidator).validateId(anyLong());
		when(tagDao.findById(anyLong())).thenReturn(Optional.of(tag1));
		TagDto actual = tagService.findTagById(id);
		assertEquals(tagDto2, actual);
	}

	@Test
	public void findTagByIdThrowIncorrectParameterValueExceptionTest() {
		final long id = 0;
		doThrow(new IncorrectParameterValueException()).when(tagValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> tagService.findTagById(id));
	}

	@Test
	public void findTagByIdThrowResourceNotFoundExceptionTest() {
		final long id = 5;
		doNothing().when(tagValidator).validateId(anyLong());
		when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> tagService.findTagById(id));
	}

	@Test
	public void findMostPopularTagPositiveTest() {
		when(tagDao.findMostPopularTagOfUserWithHighestCostOfAllOrders()).thenReturn(Optional.of(tag1));
		TagDto actual = tagService.findMostPopularTagOfUserWithHighestCostOfAllOrders();
		assertEquals(tagDto2, actual);
	}

	@Test
	public void findMostPopularTagThrowResourceNotFoundExceptionTest() {
		when(tagDao.findMostPopularTagOfUserWithHighestCostOfAllOrders()).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class,
				() -> tagService.findMostPopularTagOfUserWithHighestCostOfAllOrders());
	}

	@Test
	public void deleteTagPositiveTest() {
		final long id = 1;
		doNothing().when(tagValidator).validateId(anyLong());
		when(tagDao.delete(anyLong())).thenReturn(Boolean.TRUE);
		when(tagDao.deleteConnectionByTagId(anyLong())).thenReturn(Boolean.TRUE);
		assertDoesNotThrow(() -> tagService.deleteTag(id));
	}

	@Test
	public void deleteTagThrowIncorrectParameterValueExceptionTest() {
		final long id = -5;
		doThrow(new IncorrectParameterValueException()).when(tagValidator).validateId(anyLong());
		assertThrows(IncorrectParameterValueException.class, () -> tagService.deleteTag(id));
	}

	@Test
	public void deleteTagThrowResourceNotFoundExceptionTest() {
		final long id = 10;
		doNothing().when(tagValidator).validateId(anyLong());
		when(tagDao.delete(anyLong())).thenReturn(Boolean.FALSE);
		assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(id));
	}
}
