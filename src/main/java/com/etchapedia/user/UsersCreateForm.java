package com.etchapedia.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersCreateForm {
	@NotEmpty(message="사용자 이름은 필수항목입니다.")
	private String userName;
	
	@NotEmpty(message="이메일은 필수항목입니다.")
	private String userEmail;
	
	@NotEmpty(message="비밀번호는 필수항목입니다.")
	private String userPw;
	
}
