package com.epam.esm.dto;

/**
 * Class is implementation of pattern DTO for page entity
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
import java.util.Collections;
import java.util.List;

public class PageDto<T> {

	private List<T> pagePositions;
	private long totalNumberPositions;

	public PageDto(List<T> pagePositions, long totalNumberPositions) {
		this.pagePositions = pagePositions;
		this.totalNumberPositions = totalNumberPositions;
	}

	public List<T> getPagePositions() {
		return pagePositions == null ? null : Collections.unmodifiableList(pagePositions);
	}

	public void setPagePositions(List<T> pagePositions) {
		this.pagePositions = pagePositions;
	}

	public long getTotalNumberPositions() {
		return totalNumberPositions;
	}

	public void setTotalNumberPositions(long totalNumberPositions) {
		this.totalNumberPositions = totalNumberPositions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pagePositions == null) ? 0 : pagePositions.hashCode());
		result = prime * result + (int) (totalNumberPositions ^ (totalNumberPositions >>> 32));
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
		PageDto<?> other = (PageDto<?>) obj;
		if (pagePositions == null) {
			if (other.pagePositions != null)
				return false;
		} else if (!pagePositions.equals(other.pagePositions))
			return false;
		if (totalNumberPositions != other.totalNumberPositions)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PageDto [pagePositions=" + pagePositions + ", totalNumberPositions=" + totalNumberPositions + "]";
	}

}
