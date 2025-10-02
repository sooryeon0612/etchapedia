package com.etchapedia.pay;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etchapedia.book.Book;
import com.etchapedia.user.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private CartRepository cRepo;

    // 1️. 장바구니에 책 추가
    @Transactional
    public void addBookToCart(Book book, Users user) {
        Optional<Cart> existingCartOpt = cRepo.findByUserAndBook(user, book);

        if (existingCartOpt.isPresent()) {
            Cart existingCart = existingCartOpt.get();
            existingCart.setQuantity(existingCart.getQuantity() + 1);
        } else {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setBook(book);
            cart.setQuantity(1);
            cRepo.save(cart);
        }
    }

    // 2️. 장바구니 목록 조회
    public List<Cart> getCartItemsByUser(Users user) {
        return cRepo.findByUser(user);
    }

    // 3️. 장바구니 총액 계산
    public long calculateTotalPrice(List<Cart> cartItems) {
        return cartItems.stream()
                        .mapToLong(item -> item.getBook().getPrice() * item.getQuantity())
                        .sum();
    }

    // 4️. 장바구니 수량 변경
    @Transactional
    public void updateCartItemQuantity(Users user, Book book, int change) {
        Optional<Cart> cartOpt = cRepo.findByUserAndBook(user, book);

        cartOpt.ifPresent(cart -> {
            int newQuantity = cart.getQuantity() + change;
            if (newQuantity <= 0) {
                cRepo.delete(cart);
            } else {
                cart.setQuantity(newQuantity);
            }
        });
    }

    // 5️. 선택 삭제
    @Transactional
    public void deleteCartItem(Cart cart) {
        cRepo.delete(cart);
    }

    
	public int getCartItemCount(Users user) {
		return cRepo.countByUser(user);
	}
	
	// 결제 후 장바구니 전체 삭제
	@Transactional
	public void clearCartByUser(Users user) {
		cRepo.deleteByUser(user);
	}
}
