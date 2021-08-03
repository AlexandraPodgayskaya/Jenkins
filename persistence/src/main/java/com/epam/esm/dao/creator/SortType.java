package com.epam.esm.dao.creator;

/**
 * Class sort types
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public enum SortType {

	NAME("name"), CREATE_DATE("createDate");

	private final String sortFieldName;

	private SortType(String sortFieldName) {
		this.sortFieldName = sortFieldName;
	}

	public String getSortFieldName() {
		return sortFieldName;
	}

}
