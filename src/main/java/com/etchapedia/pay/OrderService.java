package com.etchapedia.pay;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etchapedia.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository oRepo;
    private final OrderItemRepository oiRepo;
    private final CartService cSvc;

    @Transactional
    public Orders createOrderFromCart(Users user) {
        // 1. 장바구니 가져오기
        List<Cart> cartItems = cSvc.getCartItemsByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        // 2. 주문 객체 생성
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(cSvc.calculateTotalPrice(cartItems));

        // 요약 이름: 첫 번째 상품 + 외 n건
        String summaryName = cartItems.get(0).getBook().getTitle();
        if (cartItems.size() > 1) {
            summaryName += " 외 " + (cartItems.size() - 1) + "건";
        }
        order.setSummaryName(summaryName);

        // 3. OrderItem 생성 및 주문에 추가
        for (Cart cart : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setBook(cart.getBook());
            oi.setQuantity(cart.getQuantity());

            order.getItems().add(oi);
        }

        // 4. DB 저장 (Order 저장 시 cascade로 OrderItem도 같이 저장)
        Orders savedOrder = oRepo.save(order);

        // 5. 장바구니 비우기
        for (Cart cart : cartItems) {
            cSvc.deleteCartItem(cart);
        }

        return savedOrder;
    }
}
