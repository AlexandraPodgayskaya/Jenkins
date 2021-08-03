package com.epam.esm.dao.creator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.epam.esm.entity.GiftCertificate;

/**
 * Class designed to create query
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Component
public class GiftCertificateQueryCreator {

	private static final String DELETED = "deleted";
	private final String TAGS = "tags";
	private final String NAME = "name";
	private final String DESCRIPTION = "description";
	private static final String ZERO_OR_MORE_CHARACTERS = "%";

	/**
	 * Create query
	 * 
	 * @param searchParameters the gift certificate query parameters
	 * @param criteriaBuilder  the criteria builder
	 * @return the criteria query
	 */
	public CriteriaQuery<GiftCertificate> createCriteriaQuery(GiftCertificateSearchParameters searchParameters,
			CriteriaBuilder criteriaBuilder) {
		CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
		Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
		List<Predicate> restrictions = new ArrayList<>();
		restrictions.add(addDeletedFalse(searchParameters, criteriaBuilder, giftCertificateRoot));
		restrictions.addAll(addTagNames(searchParameters, criteriaBuilder, giftCertificateRoot));
		restrictions.addAll(addPartNameOrDescription(searchParameters, criteriaBuilder, giftCertificateRoot));
		criteriaQuery.select(giftCertificateRoot).where(restrictions.toArray(new Predicate[] {}));
		addSortType(searchParameters, criteriaBuilder, criteriaQuery, giftCertificateRoot);

		return criteriaQuery;
	}

	private Predicate addDeletedFalse(GiftCertificateSearchParameters searchParameters, CriteriaBuilder criteriaBuilder,
			Root<GiftCertificate> root) {
		Predicate predicate = criteriaBuilder.equal(root.get(DELETED), Boolean.FALSE);
		return predicate;
	}

	private List<Predicate> addTagNames(GiftCertificateSearchParameters searchParameters,
			CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
		List<Predicate> restrictions = new ArrayList<>();
		if (searchParameters.getTagNames() != null) {
			restrictions = searchParameters.getTagNames().stream()
					.map(tagName -> criteriaBuilder.equal(root.join(TAGS).get(NAME), tagName))
					.collect(Collectors.toList());
		}
		return restrictions;
	}

	private List<Predicate> addPartNameOrDescription(GiftCertificateSearchParameters searchParameters,
			CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (searchParameters.getPartNameOrDescription() != null) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.like(root.get(DESCRIPTION),
							ZERO_OR_MORE_CHARACTERS + searchParameters.getPartNameOrDescription()
									+ ZERO_OR_MORE_CHARACTERS),
					criteriaBuilder.like(root.get(NAME), ZERO_OR_MORE_CHARACTERS
							+ searchParameters.getPartNameOrDescription() + ZERO_OR_MORE_CHARACTERS)));
		}
		return predicates;
	}

	private void addSortType(GiftCertificateSearchParameters searchParameters, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<GiftCertificate> criteriaQuery, Root<GiftCertificate> root) {
		if (searchParameters.getSortType() != null) {
			String sortFieldName = searchParameters.getSortType().getSortFieldName();
			if (searchParameters.getOrderType() != null && searchParameters.getOrderType().equals(OrderType.DESC)) {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortFieldName)));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortFieldName)));
			}
		}
	}
}
