package com.etchapedia.kakaopayapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartDto {
	private Integer priceNum;					// 상품번호
	private Integer priceDetailNum; 			// 상품상세번호
	private Integer cartNum; 					// 장바구니번호
	private Integer quantity; 					// 상품수량
}
