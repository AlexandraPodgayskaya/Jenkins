package com.epam.esm.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderedGiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.util.QueryParameter;

/**
 * Class is implementation of interface {@link OrderDao} and intended
 * to work with item_order table
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Repository
public class OrderDaoImpl implements OrderDao {

	private static final String SELECT_ORDER_BY_ID_SQL = "FROM Order WHERE deleted = false AND id = :id";
	private static final String SELECT_ORDERS_BY_USER_ID_SQL = "FROM Order WHERE deleted = false "
			+ "AND userId = :userId";
	private static final String SELECT_TOTAL_NUMBER_USERS_ORDERS_SQL = "SELECT COUNT(*) FROM Order "
			+ "WHERE deleted = false AND userId = :userId";
	private static final String INSERT_ORDERED_GIFT_CERTIFICATES_SQL = "INSERT INTO ordered_gift_certificate "
			+ "(order_id, gift_certificate_id, name, description, price, duration, create_date, "
			+ "last_update_date, number) VALUES (:orderId, :giftCertificateId, :name, :description, :price, "
			+ ":duration, :createDate, :lastUpdateDate, :number)";

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Order create(Order order) {
		entityManager.persist(order);
		return order;
	}

	@Override
	public void createOrderDetails(List<OrderedGiftCertificate> orderedGiftCertificates) {
		orderedGiftCertificates.forEach(order -> entityManager.createNativeQuery(INSERT_ORDERED_GIFT_CERTIFICATES_SQL)
				.setParameter(QueryParameter.ORDER_ID, order.getOrder().getId())
				.setParameter(QueryParameter.GIFT_CERTIFICATE_ID, order.getGiftCertificate().getId())
				.setParameter(QueryParameter.NAME, order.getName())
				.setParameter(QueryParameter.DESCRIPTION, order.getDescription())
				.setParameter(QueryParameter.PRICE, order.getPrice())
				.setParameter(QueryParameter.DURATION, order.getDuration())
				.setParameter(QueryParameter.CREATE_DATE, order.getCreateDate())
				.setParameter(QueryParameter.LAST_UPDATE_DATE, order.getLastUpdateDate())
				.setParameter(QueryParameter.NUMBER, order.getNumber()).executeUpdate());
	}

	@Override
	public Optional<Order> findById(long id) {
		return entityManager.createQuery(SELECT_ORDER_BY_ID_SQL, Order.class).setParameter(QueryParameter.ID, id)
				.getResultStream().findFirst();
	}

	@Override
	public List<Order> findByUserId(long userId, Pagination pagination) {
		return entityManager.createQuery(SELECT_ORDERS_BY_USER_ID_SQL, Order.class)
				.setParameter(QueryParameter.USER_ID, userId).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public long getTotalNumberByUserId(long userId) {
		return (Long) entityManager.createQuery(SELECT_TOTAL_NUMBER_USERS_ORDERS_SQL)
				.setParameter(QueryParameter.USER_ID, userId).getSingleResult();
	}
}
