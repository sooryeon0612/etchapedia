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


$(function () {
	// 이름 수정 
	$("#edit-btn").click(function() {
		enterEdit();
	})
	$("#cancel-btn").click(function() {
		exitEdit();
	})

	
	
	
	
	
	
	
	
	
	
  	const $panel = $('#passwordPanel');
  	const $toggle = $('#pwToggleBtn');
  	const $cancel = $('#pwCancelBtn');

  	$toggle.on('click', function () {
    	$panel.toggleClass('open');
  	});

  	$cancel.on('click', function () {
    	$panel.removeClass('open');
  	});
});
