package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.epam.esm.entity.audit.OrderAudit;

/**
 * Class represents item_order entity
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Entity
@Table(name = "item_order")
@EntityListeners(OrderAudit.class)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "cost")
	private BigDecimal cost;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "create_date")
	private LocalDateTime createDate;
	@Column(name = "deleted")
	private boolean deleted;

	@OneToMany(mappedBy = "order")
	private List<OrderedGiftCertificate> orderedGiftCertificates;

	public Order() {
	}

	public Order(Long id, BigDecimal cost, Long userId, LocalDateTime createDate, boolean deleted,
			List<OrderedGiftCertificate> orderedGiftCertificates) {
		this.id = id;
		this.cost = cost;
		this.userId = userId;
		this.createDate = createDate;
		this.deleted = deleted;
		this.orderedGiftCertificates = orderedGiftCertificates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public List<OrderedGiftCertificate> getOrderedGiftCertificates() {
		return orderedGiftCertificates;
	}

	public void setOrderedGiftCertificates(List<OrderedGiftCertificate> orderedGiftCertificates) {
		this.orderedGiftCertificates = orderedGiftCertificates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orderedGiftCertificates == null) ? 0 : orderedGiftCertificates.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		Order other = (Order) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (deleted != other.deleted)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderedGiftCertificates == null) {
			if (other.orderedGiftCertificates != null)
				return false;
		} else if (!orderedGiftCertificates.equals(other.orderedGiftCertificates))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", cost=" + cost + ", userId=" + userId + ", createDate=" + createDate + ", deleted="
				+ deleted + ", orderedGiftCertificates=" + orderedGiftCertificates + "]";
	}

}
