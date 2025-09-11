package com.etchapedia.security;

import lombok.Getter;

@Getter
public enum MemberRole {
	ADMIN("ROLE_ADMIN"), // MemeberRole r1 = MemberRole.ADMIN; 과 같은 식으로 사용.
	USER("ROLE_USER"); // MemberRole r2 = MemberRole.USER;

	private String value;
	
	MemberRole(String value) {
		this.value = value;
	}

}
