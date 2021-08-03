package com.epam.esm.entity;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Class represents roles with authorities
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public enum Role {
	ADMIN(Set.of(Permission.AUTHORITY_WRITE, Permission.AUTHORITY_READ, Permission.AUTHORITY_READ_ALL,
			Permission.AUTHORITY_MAKE_ORDER)),
	USER(Set.of(Permission.AUTHORITY_READ, Permission.AUTHORITY_MAKE_ORDER));

	private final Set<Permission> permissions;

	Role(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Get permissions
	 * 
	 * @return set of permissions
	 */
	public Set<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * Get authorities
	 * 
	 * @return set of SimpleGrantedAuthority
	 */
	public Set<SimpleGrantedAuthority> getAuthorities() {
		return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());
	}

}
