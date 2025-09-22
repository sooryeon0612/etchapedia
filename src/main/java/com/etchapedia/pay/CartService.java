package com.etchapedia.pay;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etchapedia.book.Book;
import com.etchapedia.user.Users;

@Service
public class CartService {
    @Autowired
    private CartRepository cRepo;
    
    public CartService(CartRepository cartRepository) {
        this.cRepo = cartRepository;
    }

//    public void addBookToCart(Book book, Users user) {
//        // 장바구니 항목을 생성하고 책과 사용자 정보를 설정합니다.
//        Cart cartItem = new Cart();
//        cartItem.setBook(book);
//        cartItem.setUser(user);
//        
//        // 데이터베이스에 장바구니 항목을 저장합니다.
//        cRepo.save(cartItem);
//    }
    
    public void addBookToCart(Book book, Users user) {
        // 1. 이미 장바구니에 해당 책이 있는지 확인
        Optional<Cart> existingCartItem = cRepo.findByUserAndBook(user, book);

        if (existingCartItem.isPresent()) {
            // 2. 이미 존재하면 수량만 증가
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cRepo.save(cartItem);
        } else {
            // 3. 존재하지 않으면 새로운 상품 추가
            Cart newCartItem = new Cart();
            newCartItem.setBook(book);
            newCartItem.setUser(user);
            newCartItem.setQuantity(1); // 기본 수량은 1
            cRepo.save(newCartItem);
        }
    }
    
    public int getCartItemCount(Users user) {
    	return cRepo.countByUser(user);
    }

	public Long calculateTotalPrice(List<Cart> cartItems) {
		Long totalPrice = 0L;
        for (Cart item : cartItems) {
            // 상품 가격 * 수량
            totalPrice += (long) item.getBook().getPrice() * item.getQuantity();
        }
        return totalPrice;
	}

	public List<Cart> getCartItemsByUser(Users user) {
		return cRepo.findByUser(user);
	}

	public void updateCartItemQuantity(Users user, Book book, int change) {
        Optional<Cart> optionalCartItem = cRepo.findByUserAndBook(user, book);
        if (optionalCartItem.isPresent()) {
            Cart cartItem = optionalCartItem.get();
            int newQuantity = cartItem.getQuantity() + change;

            if (newQuantity <= 0) {
                // 수량이 0 이하면 장바구니에서 상품 제거
                cRepo.delete(cartItem);
            } else {
                // 수량 업데이트
                cartItem.setQuantity(newQuantity);
                cRepo.save(cartItem);
            }
        }
    }
}