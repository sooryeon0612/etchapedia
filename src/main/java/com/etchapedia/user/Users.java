package com.etchapedia.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Users {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user")
	@SequenceGenerator(name="user", sequenceName="seq_user_idx", allocationSize=1)
	private Integer userIdx;
	
	private String email;
	private String name;
	private String password;
	private String profile;
	private String code;
}