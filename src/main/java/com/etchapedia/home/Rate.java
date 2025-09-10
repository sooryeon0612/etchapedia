package com.etchapedia.home;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Rate {
	// FK
	private Integer bookIdx;
	
	// FK
	private Integer userIdx;
	
	private Integer rate;
}
