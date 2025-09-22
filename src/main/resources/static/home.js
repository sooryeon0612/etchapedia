function loadRecommendBooks(userIdx) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
	
	const json_data = {
		userIdx : userIdx
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/load_recommend_books", init)
	.then(response => response.json())
	.then(data => {
		$(".loading-box").remove();
		let str = "";
		data.forEach(function(book, index) {
			str = `	<div class="rank-item">
                    	<div class="rank-number">${index + 1}</div>
						<a th:href="@{/detail/page(id=${book.bookIdx})}">
	                    	<img src="${book.pic}" alt="${book.title}" class="book-cover">
						</a>
	                    <div class="book-info">
                        	<h3 class="book-title">${book.title}</h3>
	                        <div class="author">${book.author}</div>
	                    </div>
	                </div>`;
			$(".recommend-list").append(str);
		})
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}


$(function() {
	const userIdx = $(".user-menu").data("userIdx");
	
	if(userIdx != null) loadRecommendBooks(userIdx);
});