package com.etchapedia.pay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartItemDto {
	private	String name;			// 상품명
	private int totalPrice; 		// 가격
	private int count; 				// 수량
	private String pic;				// 사진
	private String delivery;		// 배송 종류
	
	
	public CartItemDto(String name, int totalPrice, int count, String pic, String delivery) {
	}


	public int getQuantity() {
		// TODO Auto-generated method stub
		return 0;
	}
}
