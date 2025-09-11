package com.etchapedia.security;

import lombok.Getter;

@Getter
public enum UsersRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	private String value;
	
	UsersRole(String value) {
		this.value = value;
	}
}
