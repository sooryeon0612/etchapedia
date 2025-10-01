// CSRF 토큰
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

// cartItems와 totalPrice는 HTML에서 data-* 속성으로 전달
const cartItems = JSON.parse(document.getElementById('cart-data').dataset.items || '[]');
let totalPrice = parseInt(document.getElementById('cart-data').dataset.total || '0', 10);

// 수량 증가
function increaseQuantity(bookId) {
    const itemElement = document.getElementById('item-' + bookId);
    const quantityInput = itemElement.querySelector('.quantity-input');

    fetch('/cart/update-quantity', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ bookIdx: bookId, change: 1 })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            let newQuantity = parseInt(quantityInput.value) + 1;
            quantityInput.value = newQuantity;
            updateTotalPrice();
        }
    });
}

// 수량 감소
function decreaseQuantity(bookId) {
    const itemElement = document.getElementById('item-' + bookId);
    const quantityInput = itemElement.querySelector('.quantity-input');

    fetch('/cart/update-quantity', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ bookIdx: bookId, change: -1 })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            let newQuantity = parseInt(quantityInput.value) - 1;
            if (newQuantity <= 0) {
                itemElement.remove();
            } else {
                quantityInput.value = newQuantity;
            }
            updateTotalPrice();
        }
    });
}

// 총 합계 업데이트
function updateTotalPrice() {
    let newTotal = 0;
    document.querySelectorAll('.cart-item').forEach(item => {
        const quantity = parseInt(item.querySelector('.quantity-input').value);
        const priceText = item.querySelector('.original-price').textContent;
        const price = parseInt(priceText.replace(/,/g, '').replace('원', ''));
        newTotal += quantity * price;
    });

    document.querySelectorAll('.total-price').forEach(el => {
        el.textContent = newTotal.toLocaleString() + '원';
    });
}

// 카카오페이 결제
document.addEventListener('DOMContentLoaded', () => {
    const orderBtn = document.getElementById('order-btn');
    if (!orderBtn) return;

    orderBtn.addEventListener('click', () => {
        $.ajax({
            url: "/cart/order",
            type: "POST",
            success: function(res) {
                if (res.success) {
                    $.ajax({
                        url: "/order/pay/ready",
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(res.orderRequest),
                        success: function(payRes) {
                            if (payRes.next_redirect_pc_url) {
                                window.location.href = payRes.next_redirect_pc_url;
                            } else {
                                alert("결제 준비 실패");
                            }
                        },
                        error: function(xhr) {
                            console.error("pay/ready error:", xhr.status, xhr.responseText);
                        }
                    });
                } else {
                    alert(res.message);
                }
            },
            error: function(xhr) {
                console.error("cart/order error:", xhr.status, xhr.responseText);
            }
        });
    });
});
