package com.epam.esm.entity;

import java.io.Serializable;
/**
 * Class represents ordered gift certificate id
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public class OrderedGiftCertificateId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Order order;
	private GiftCertificate giftCertificate;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public GiftCertificate getGiftCertificate() {
		return giftCertificate;
	}

	public void setGiftCertificate(GiftCertificate giftCertificate) {
		this.giftCertificate = giftCertificate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((giftCertificate == null) ? 0 : giftCertificate.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
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
		OrderedGiftCertificateId other = (OrderedGiftCertificateId) obj;
		if (giftCertificate == null) {
			if (other.giftCertificate != null)
				return false;
		} else if (!giftCertificate.equals(other.giftCertificate))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderedGiftCertificateId [order=" + order + ", giftCertificate=" + giftCertificate + "]";
	}

}
