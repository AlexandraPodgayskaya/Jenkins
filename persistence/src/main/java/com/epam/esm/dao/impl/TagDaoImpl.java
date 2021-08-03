package com.epam.esm.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.QueryParameter;

/**
 * Class is implementation of interface {@link TagDao} and intended to work with
 * tag table
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Repository
public class TagDaoImpl implements TagDao {

	private static final String SELECT_ALL_TAGS_SQL = "FROM Tag WHERE deleted = false";
	private static final String SELECT_TAG_BY_ID_SQL = "FROM Tag WHERE deleted = false AND id = :id";
	private static final String SELECT_TAG_BY_NAME_SQL = "FROM Tag WHERE deleted = false AND name = :name";
	private static final String SELECT_MOST_POPULAR_TAG_SQL = "SELECT tag.id, tag.name, tag.deleted "
			+ "FROM item_order JOIN ordered_gift_certificate ON item_order.id = ordered_gift_certificate.order_id "
			+ "JOIN gift_certificate ON ordered_gift_certificate.gift_certificate_id = gift_certificate.id "
			+ "JOIN gift_certificate_tag_connection ON gift_certificate.id = gift_certificate_tag_connection.gift_certificate_id "
			+ "JOIN tag ON gift_certificate_tag_connection.tag_id = tag.id WHERE item_order.deleted = false "
			+ "AND tag.deleted = false AND item_order.user_id = (SELECT item_order.user_id from item_order "
			+ "JOIN user ON item_order.user_id = user.id WHERE item_order.deleted = false and user.deleted = false "
			+ "GROUP BY user_id ORDER BY sum(item_order.cost) DESC LIMIT 1) GROUP BY tag.id ORDER BY count(*) DESC LIMIT 1 ";
	private static final String DELETE_TAG_SQL = "UPDATE Tag SET deleted = true WHERE id = :id AND deleted = false";
	private static final String DELETE_CONNECTION_BY_TAG_ID_SQL = "DELETE FROM "
			+ "gift_certificate_tag_connection WHERE tag_id = :tagId";
	private static final String SELECT_TOTAL_NUMBER_TAGS_SQL = "SELECT COUNT(*) FROM Tag WHERE deleted = false";

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Tag create(Tag tag) {
		entityManager.persist(tag);
		return tag;
	}

	@Override
	public List<Tag> findAll(Pagination pagination) {
		return entityManager.createQuery(SELECT_ALL_TAGS_SQL, Tag.class).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public Optional<Tag> findById(long id) {
		return entityManager.createQuery(SELECT_TAG_BY_ID_SQL, Tag.class).setParameter(QueryParameter.ID, id)
				.getResultStream().findFirst();
	}

	@Override
	public Optional<Tag> findByName(String tagName) {
		return entityManager.createQuery(SELECT_TAG_BY_NAME_SQL, Tag.class).setParameter(QueryParameter.NAME, tagName)
				.getResultStream().findFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Tag> findMostPopularTagOfUserWithHighestCostOfAllOrders() {
		return entityManager.createNativeQuery(SELECT_MOST_POPULAR_TAG_SQL, Tag.class).getResultStream().findFirst();
	}

	@Override
	public boolean delete(long id) {
		return entityManager.createQuery(DELETE_TAG_SQL).setParameter(QueryParameter.ID, id).executeUpdate() == 1;
	}

	@Override
	public boolean deleteConnectionByTagId(long id) {
		return entityManager.createNativeQuery(DELETE_CONNECTION_BY_TAG_ID_SQL).setParameter(QueryParameter.TAG_ID, id)
				.executeUpdate() > 0;
	}

	@Override
	public long getTotalNumber() {
		return (Long) entityManager.createQuery(SELECT_TOTAL_NUMBER_TAGS_SQL).getSingleResult();
	}

}
