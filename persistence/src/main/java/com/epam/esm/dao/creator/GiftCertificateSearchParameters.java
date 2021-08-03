package com.epam.esm.dao.creator;

import java.util.Collections;
import java.util.List;

/**
 * Class consists parameters which are used to make selection to database.
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public class GiftCertificateSearchParameters {
	private List<String> tagNames;
	private String partNameOrDescription;
	private SortType sortType;
	private OrderType orderType;

	public GiftCertificateSearchParameters() {
	}

	public GiftCertificateSearchParameters(List<String> tagNames, String partNameOrDescription) {
		this.tagNames = tagNames;
		this.partNameOrDescription = partNameOrDescription;
	}

	public List<String> getTagNames() {
		return tagNames == null ? null : Collections.unmodifiableList(tagNames);
	}

	public void setTagNames(List<String> tagNames) {
		this.tagNames = tagNames;
	}

	public String getPartNameOrDescription() {
		return partNameOrDescription;
	}

	public void setPartNameOrDescription(String partNameOrDescription) {
		this.partNameOrDescription = partNameOrDescription;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + ((partNameOrDescription == null) ? 0 : partNameOrDescription.hashCode());
		result = prime * result + ((sortType == null) ? 0 : sortType.hashCode());
		result = prime * result + ((tagNames == null) ? 0 : tagNames.hashCode());
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
		GiftCertificateSearchParameters other = (GiftCertificateSearchParameters) obj;
		if (orderType != other.orderType)
			return false;
		if (partNameOrDescription == null) {
			if (other.partNameOrDescription != null)
				return false;
		} else if (!partNameOrDescription.equals(other.partNameOrDescription))
			return false;
		if (sortType != other.sortType)
			return false;
		if (tagNames == null) {
			if (other.tagNames != null)
				return false;
		} else if (!tagNames.equals(other.tagNames))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GiftCertificateSearchParameters [tagNames=" + tagNames + ", partNameOrDescription="
				+ partNameOrDescription + ", sortType=" + sortType + ", orderType=" + orderType + "]";
	}

}
