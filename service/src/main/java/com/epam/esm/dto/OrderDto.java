package com.epam.esm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.epam.esm.dto.deserializer.UserIdDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class is implementation of pattern DTO for transmission order entity between
 * service and controller
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public class OrderDto extends RepresentationModel<OrderDto> {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	@JsonProperty(access = Access.READ_ONLY)
	private BigDecimal cost;
	@JsonDeserialize(using = UserIdDeserializer.class)
	private Long userId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime createDate;
	List<OrderedGiftCertificateDto> orderedGiftCertificates;

	public OrderDto() {
	}

	public OrderDto(Long userId, List<OrderedGiftCertificateDto> orderedGiftCertificates) {
		this.userId = userId;
		this.orderedGiftCertificates = orderedGiftCertificates;
	}

	public OrderDto(Long id, BigDecimal cost, Long userId, LocalDateTime createDate,
			List<OrderedGiftCertificateDto> orderedGiftCertificates) {
		this.id = id;
		this.cost = cost;
		this.userId = userId;
		this.createDate = createDate;
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

	public List<OrderedGiftCertificateDto> getOrderedGiftCertificates() {
		return orderedGiftCertificates == null ? null : Collections.unmodifiableList(orderedGiftCertificates);
	}

	public void setOrderedGiftCertificates(List<OrderedGiftCertificateDto> orderedGiftCertificates) {
		this.orderedGiftCertificates = orderedGiftCertificates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orderedGiftCertificates == null) ? 0 : orderedGiftCertificates.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDto other = (OrderDto) obj;
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
		return "OrderDto [id=" + id + ", cost=" + cost + ", userId=" + userId + ", createDate=" + createDate
				+ ", orderedGiftCertificates=" + orderedGiftCertificates + "]";
	}

}
