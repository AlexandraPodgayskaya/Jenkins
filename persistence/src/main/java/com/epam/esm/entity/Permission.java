package com.epam.esm.entity;

/**
 * Class permissions
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public enum Permission {
	AUTHORITY_READ("authority:read"), AUTHORITY_READ_ALL("authority:read_all"), AUTHORITY_WRITE("authority:write"),
	AUTHORITY_MAKE_ORDER("authority:make_order");

	private final String permission;

	Permission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

}
