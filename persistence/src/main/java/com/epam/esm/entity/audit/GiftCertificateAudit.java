package com.epam.esm.entity.audit;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.epam.esm.entity.GiftCertificate;

/**
 * Class is listener for gift certificate entity
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public class GiftCertificateAudit {

	@PrePersist
	public void beforeCreateGiftCertificate(GiftCertificate giftCertificate) {
		LocalDateTime currentTime = LocalDateTime.now();
		giftCertificate.setCreateDate(currentTime);
		giftCertificate.setLastUpdateDate(currentTime);
	}

	@PreUpdate
	public void beforeUpdateGiftCertificate(GiftCertificate giftCertificate) {
		giftCertificate.setLastUpdateDate(LocalDateTime.now());
	}

}
