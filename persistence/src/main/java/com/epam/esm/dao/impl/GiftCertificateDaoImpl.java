package com.epam.esm.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.creator.GiftCertificateQueryCreator;
import com.epam.esm.dao.creator.GiftCertificateSearchParameters;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.util.QueryParameter;

/**
 * Class is implementation of interface {@link GiftCertificateDao} and intended
 * to work with gift_certificate table
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

	private static final String SELECT_GIFT_CERTIFICATE_BY_ID_SQL = "FROM GiftCertificate WHERE deleted = false "
			+ "AND id = :id";
	private static final String SELECT_GIFT_CERTIFICATE_BY_NAME_SQL = "FROM GiftCertificate WHERE deleted = false "
			+ "AND name = :name";
	private static final String DELETE_GIFT_CERTIFICATE_SQL = "UPDATE GiftCertificate SET deleted = true "
			+ "WHERE id = :id AND deleted = false";
	private static final String DELETE_CONNECTION_BY_GIFT_CERTIFICATE_ID_SQL = "DELETE FROM "
			+ "gift_certificate_tag_connection WHERE gift_certificate_id = :giftCertificateId";

	private final GiftCertificateQueryCreator queryCreator;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public GiftCertificateDaoImpl(GiftCertificateQueryCreator queryCreator) {
		this.queryCreator = queryCreator;
	}

	@Override
	public GiftCertificate create(GiftCertificate giftCertificate) {
		entityManager.persist(giftCertificate);
		return giftCertificate;
	}

	@Override
	public List<GiftCertificate> findBySearchParameters(Pagination pagination,
			GiftCertificateSearchParameters giftCertificateSearchParameters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> criteriaQuery = queryCreator.createCriteriaQuery(giftCertificateSearchParameters,
				criteriaBuilder);
		return entityManager.createQuery(criteriaQuery).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public Optional<GiftCertificate> findById(long id) {
		return entityManager.createQuery(SELECT_GIFT_CERTIFICATE_BY_ID_SQL, GiftCertificate.class)
				.setParameter(QueryParameter.ID, id).getResultStream().findFirst();
	}

	@Override
	public Optional<GiftCertificate> findByName(String name) {
		return entityManager.createQuery(SELECT_GIFT_CERTIFICATE_BY_NAME_SQL, GiftCertificate.class)
				.setParameter(QueryParameter.NAME, name).getResultStream().findFirst();
	}

	@Override
	public GiftCertificate update(GiftCertificate giftCertificate) {
		giftCertificate = entityManager.merge(giftCertificate);
		entityManager.flush();
		return giftCertificate;
	}

	@Override
	public boolean delete(long id) {
		return entityManager.createQuery(DELETE_GIFT_CERTIFICATE_SQL).setParameter(QueryParameter.ID, id)
				.executeUpdate() == 1;
	}

	@Override
	public boolean deleteConnectionByGiftCertificateId(long id) {
		return entityManager.createNativeQuery(DELETE_CONNECTION_BY_GIFT_CERTIFICATE_ID_SQL)
				.setParameter(QueryParameter.GIFT_CERTIFICATE_ID, id).executeUpdate() > 0;
	}

	@Override
	public long getTotalNumber(GiftCertificateSearchParameters searchParameters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> criteriaQuery = queryCreator.createCriteriaQuery(searchParameters,
				criteriaBuilder);
		return (long) entityManager.createQuery(criteriaQuery).getResultStream().count();
	}
}
