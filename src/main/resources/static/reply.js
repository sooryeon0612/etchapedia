function loadReplyLikes(commentIdx, userIdx) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

	const json_data = {
		commentIdx : commentIdx
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/load_reply_likes", init)
	.then(response => response.json())
	.then(data => {
		$(".reply").each(function() {
			const replyIdx = $(this).data("reply-idx");
			const countLikes = data[replyIdx].length;
			$(this).find(".count-likes").text(countLikes);
			for(let i=0; i<countLikes; i++){
				if(data[replyIdx][i].user.userIdx == userIdx) {
					$(this).find("i").toggleClass("active");
					break;
				}
			}
		})
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

function insertCommentLike(userIdx, commentIdx) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

	const json_data = {
		userIdx : userIdx,
		commentIdx : commentIdx
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/insert_comment_like", init)
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

function deleteCommentLike(userIdx, commentIdx) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

	const json_data = {
		userIdx : userIdx,
		commentIdx : commentIdx
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/delete_comment_like", init)
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

function insertReplyLike(userIdx, replyIdx) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

	const json_data = {
		userIdx : userIdx,
		replyIdx : replyIdx
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/insert_reply_like", init)
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

function deleteReplyLike(userIdx, replyIdx) {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

	const json_data = {
		userIdx : userIdx,
		replyIdx : replyIdx
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/delete_reply_like", init)
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

function isTextExist(invoke) {
	const text = $(invoke).find(".reply-input-field").val().trim();
	if(text == "") return false;
	return true;
}

$(function() {
	const urlParams = new URLSearchParams(location.search);
	const commentIdx = urlParams.get("commentIdx");
	const userIdx = $(".user-menu").data("userIdx");
	
	loadReplyLikes(commentIdx, userIdx);
	
	// 좋아요 누르기
	$(".thumbs").on("click", function() {
		const countLikes = Number($(this).find("span").text());
		const $i = $(this).find("i");
		$i.toggleClass("active");
		if($i.hasClass("active")) {
			$(this).find("span").text(countLikes + 1);
			const replyIdx = Number($(this).closest(".reply").data("reply-idx"));
			if(isNaN(replyIdx)) {
				insertCommentLike(userIdx, commentIdx);
			} else insertReplyLike(userIdx, replyIdx);
		}
		else {
			$(this).find("span").text(countLikes - 1);
			const replyIdx = Number($(this).closest(".reply").data("reply-idx"));
			if(isNaN(replyIdx)) {
				deleteCommentLike(userIdx, commentIdx);
			} else deleteReplyLike(userIdx, replyIdx);
		}
	});
	
	// 댓글 달기
	$(".reply-input-field").on("keypress", function(e) {
		if(e.keyCode == "Enter") $(this).closest("form").submit();
	})
	
})