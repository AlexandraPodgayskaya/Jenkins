package com.epam.esm.dto;

/**
 * Class is implementation of pattern DTO for transmission information about
 * pagination between service and controller
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public class PaginationDto {

	private int offset;
	private int limit;

	public PaginationDto(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + limit;
		result = prime * result + offset;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaginationDto other = (PaginationDto) obj;
		if (limit != other.limit)
			return false;
		if (offset != other.offset)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PaginationDto [offset=" + offset + ", limit=" + limit + "]";
	}

}
