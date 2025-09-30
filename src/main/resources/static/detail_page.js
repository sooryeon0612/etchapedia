document.addEventListener("DOMContentLoaded", () => {
    // 장바구니
    window.addToCart = function(bookId) {
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
        const cartCountElement = document.getElementById('cart-item-count');

        fetch('/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ bookIdx: bookId })
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert('장바구니에 담았습니다!');
                if (cartCountElement) {
                    let currentCount = parseInt(cartCountElement.textContent) || 0;
                    cartCountElement.textContent = currentCount + 1;
                }
            } else {
                alert('장바구니 담기에 실패했습니다: ' + data.message);
            }
        })
        .catch(err => {
            console.error('Error:', err);
            alert('장바구니 담기 중 오류가 발생했습니다.');
        });
    };

    // 결제하기
    window.addAndCheckout = function(bookId) {
        alert('결제 페이지로 이동합니다!');
        window.location.href = '/cart';
    };

    // 코멘트 팝업 열기/닫기
    window.showCommentPopup = function() {
        const popup = document.getElementById("comment-popup");
        if (popup) popup.style.display = "flex";
    };

    window.closeCommentPopup = function() {
        const popup = document.getElementById("comment-popup");
        if (popup) popup.style.display = "none";
    };

    // 코멘트 저장
    const saveBtn = document.querySelector(".save-btn");
    const textarea = document.querySelector("#comment-popup textarea");
    const charCount = document.querySelector(".char-count");
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    if (textarea) {
        textarea.addEventListener("input", () => {
            charCount.textContent = `${textarea.value.length}/10000`;
        });
    }

    if (saveBtn) {
        saveBtn.addEventListener("click", () => {
            const content = textarea.value.trim();
            if (content.length === 0) {
                alert("내용을 입력해주세요!");
                return;
            }

            fetch("/comments", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({
                    content: content,
                    bookIdx: document.querySelector("main.main-content")?.dataset.bookId
                })
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    appendComment(data.comment);
                    textarea.value = "";
                    charCount.textContent = "0/10000";
                    closeCommentPopup();
                } else {
                    alert("저장 실패: " + data.message);
                }
            })
            .catch(err => {
                console.error("Error:", err);
                alert("저장 중 오류 발생");
            });
        });
    }

    // 관심없어요 버튼
    const btn = document.getElementById("disinterest-button");
    if (btn) {
        const icon = btn.querySelector("i");
        btn.addEventListener("click", () => {
            const bookId = btn.dataset.bookId;
            const wasHated = btn.dataset.isHated === "true"; 

            toggleIconUI(icon, btn, !wasHated);

            toggleDisinterest(bookId, !wasHated)
                .catch(err => {
                    console.error(err);
                    alert("처리 중 오류가 발생했습니다.");
                    toggleIconUI(icon, btn, wasHated); // 롤백
                });
        });
    }
});

// 코멘트 추가 DOM
function appendComment(comment) {
    const grid = document.querySelector(".comments-grid");
    const div = document.createElement("div");
    div.classList.add("comment-item");
    div.innerHTML = `
        <div class="comment-header">
            <img src="https://via.placeholder.com/30" alt="프로필 이미지">
            <span class="user-name">${comment.user.name}</span>
        </div>
        <p class="comment-content">${comment.content}</p>
        <div class="comment-actions">
            <div class="action-buttons">
                <button class="like-btn"><i class="fa-regular fa-thumbs-up"></i>좋아요</button>
                <button class="reply-btn" onclick="showCommentPopup()"><i class="fa-regular fa-comment"></i>댓글</button>
            </div>
        </div>
    `;
    if (grid) {
        grid.prepend(div);
    }
}

// 관심없어요 UI 토글
function toggleIconUI(icon, btn, isHated) {
    if (isHated) {
        icon.classList.remove("fa-regular");
        icon.classList.add("fa-solid");
        btn.dataset.isHated = "true";
    } else {
        icon.classList.remove("fa-solid");
        icon.classList.add("fa-regular");
        btn.dataset.isHated = "false";
    }
}

// 서버 요청
function toggleDisinterest(bookId, isHated) {
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;

    return fetch(`/book/disinterest`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify({ bookId, isHated })
    })
    .then(res => {
        if (!res.ok) throw new Error("관심없어요 반영 실패");
        return res.json();
    });
}
