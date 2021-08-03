package com.epam.esm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.epam.esm.dto.deserializer.NumberDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class is implementation of pattern DTO for transmission ordered gift
 * certificate entity between service and controller
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public class OrderedGiftCertificateDto extends RepresentationModel<OrderDto> {

	@JsonProperty(access = Access.WRITE_ONLY)
	private GiftCertificateDto giftCertificate;
	private String name;
	private String description;
	@JsonProperty(access = Access.READ_ONLY)
	private BigDecimal price;
	@JsonProperty(access = Access.READ_ONLY)
	private Integer duration;
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime createDate;
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime lastUpdateDate;
	@JsonDeserialize(using = NumberDeserializer.class)
	private Integer number;

	public OrderedGiftCertificateDto() {
	}

	public OrderedGiftCertificateDto(GiftCertificateDto giftCertificate, Integer number) {
		this.giftCertificate = giftCertificate;
		this.number = number;
	}

	public OrderedGiftCertificateDto(GiftCertificateDto giftCertificate, String name, String description,
			BigDecimal price, Integer duration, LocalDateTime createDate, LocalDateTime lastUpdateDate,
			Integer number) {
		this.giftCertificate = giftCertificate;
		this.name = name;
		this.description = description;
		this.price = price;
		this.duration = duration;
		this.createDate = createDate;
		this.lastUpdateDate = lastUpdateDate;
		this.number = number;
	}

	public GiftCertificateDto getGiftCertificate() {
		return giftCertificate;
	}

	public void setGiftCertificate(GiftCertificateDto giftCertificate) {
		this.giftCertificate = giftCertificate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((giftCertificate == null) ? 0 : giftCertificate.hashCode());
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		OrderedGiftCertificateDto other = (OrderedGiftCertificateDto) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (giftCertificate == null) {
			if (other.giftCertificate != null)
				return false;
		} else if (!giftCertificate.equals(other.giftCertificate))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderDetailsDto [giftCertificate=" + giftCertificate + ", name=" + name + ", description=" + description
				+ ", price=" + price + ", duration=" + duration + ", createDate=" + createDate + ", lastUpdateDate="
				+ lastUpdateDate + ", number=" + number + "]";
	}

}
