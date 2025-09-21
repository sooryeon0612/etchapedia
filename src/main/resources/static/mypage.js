function enterEdit() {
	$("#display-name").css("display", "none");
	$("#name-input").css("display", "block");
	$("#edit-btn").css("display", "none");
	$("#save-btn").css("display", "inline-flex");
	$("#cancel-btn").css("display", "inline-flex");
	$("#name-input").val($("#display-name").text());
}

function exitEdit() {
	$("#display-name").css("display", "block");
	$("#name-input").css("display", "none");
	$("#edit-btn").css("display", "inline-flex");
	$("#save-btn").css("display", "none");
	$("#cancel-btn").css("display", "none");
}

function checkPw() {
	const newPw = $("#new-password").val();
	const confirmPw = $("#confirm-password").val();
	
	if(newPw == confirmPw) return true;
	
	$(".err-msg").text("비밀번호가 일치하지 않습니다.");
	return false;
}


$(function () {
	// 이름 수정 
	$("#edit-btn").click(function() {
		enterEdit();
	})
	$("#cancel-btn").click(function() {
		exitEdit();
	})
	
	// 비밀번호 변경 폼 
	$("#pw-toggle-btn").click(function() {
		$(".password-panel").toggleClass("open");
	})
	$("#pw-cancel-btn").click(function() {
		$(".password-panel").removeClass("open");
	})
	

});
