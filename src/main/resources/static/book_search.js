function loadNewBooks(search) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
	
	const json_data = {
		search : search
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/load_new_books", init)
	.then(response => response.json())
	.then(data => {
		$(".loading-box").remove();
		let str = "";
		data.forEach(function(book) {
			str = `	<div class="book-item">
					    <div class="book-cover">
							<a th:href="@{/detail/page(id=${book.bookIdx})}">
					        	<img src="${book.pic}" alt="${book.title}">
							</a>
					    </div>
					    <div class="book-info">
					        <h3 class="book-title">${book.title}</h3>
					        <p class="book-meta">${book.author}</p>
					    </div>
					</div>`;
			$(".book-grid").append(str);
		})
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

$(function() {
	const urlParams = new URLSearchParams(location.search);
	const search = urlParams.get("query");
	
	if($(".book-grid").text().trim().length === 0) {
		loadNewBooks(search);
	} else $(".loading-box").remove();
})