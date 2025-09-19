package com.etchapedia.kakaopayapi;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDto {
	private String name;					// 카카오페이에 보낼 대표 상품명
    private int totalPrice;      			// 총 결제금액
    private int usePoint;        			// 사용 포인트
    private int deliveryId;      			// 배송지 번호
    private String deliveryMemo; 			// 배송메모
    List<CartDto> items;
     					
}
