package com.etchapedia.pay;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDto {
    private String name; 						// 상품 요약 이름
    private long totalPrice; 					// 결제 금액
    private List<ItemQuantity> quantities; 		// 장바구니 항목들

    @Data
    public static class ItemQuantity {
        private Integer bookIdx;
        private Integer quantity;
    }
}
