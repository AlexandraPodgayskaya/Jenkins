package com.epam.esm.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.epam.esm.dto.JwtUser;

/**
 * Class is used to check the ability to access
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Component
public class AccessVerifier {

	/**
	 * Check access
	 * 
	 * @param userId the user id
	 * @throws AccessDeniedException if access is denied
	 */
	public void checkAccess(long userId) throws AccessDeniedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
		if (!jwtUser.isAdmin() && jwtUser.getId() != userId) {
			throw new AccessDeniedException("access is denied");
		}
	}
}
