$(function() {
	const urlParams = new URLSearchParams(location.search);
	const search = urlParams.get("query");
	$(".search-bar > input").val(search);
	
	$(".search-bar > input").keypress(function(e) {
		if(e.keyCode == 13) {
			location.href="/book/search?query=" + $(this).val();
		}
	})
})