// 이메일 중복여부 체크
function checkEmailDuplication() {
    const userEmail = document.getElementById('userEmail').value;
    const resultDiv = document.getElementById('email_check_result');

    // 이메일이 비어있으면 경고 메시지 표시
    if (!userEmail) {
        resultDiv.innerText = '이메일을 입력해주세요.';
        resultDiv.style.color = 'red';
        return;
    }

    fetch('/user/checkEmail?email=' + userEmail, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        if (data.isDuplicated) {
            resultDiv.innerText = '이미 사용 중인 이메일입니다.';
            resultDiv.style.color = 'red';
        } else {
            resultDiv.innerText = '사용 가능한 이메일입니다.';
            resultDiv.style.color = 'green';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        resultDiv.innerText = '오류가 발생했습니다.';
        resultDiv.style.color = 'red';
    });
}

// 인증코드 메시지 보내기 
function sendVerificationCode() {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
	
	const phoneNum = $("#phoneNumber").val();
	const json_data = {
		phoneNum : phoneNum
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/send_verification_code", init)
	.then(response => {
		$("#codeArea").css("display", "block");
		$(".signup-container").css("height", "590px");
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}

// 인증코드 확인
function verifyCode() {
	const csrfToken = $('meta[name="_csrf"]').attr('content');
	const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
	const json_data = {
		smsCode : $("#smsCode").val()
	}
	const init = {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(json_data)
	};
	fetch("/ajax/verify_code", init)
	.then(response => response.json())
	.then(data => {
		if(data) {
			$("#code-msg").text("인증에 성공했습니다!");
			$("#code-msg").css("color", "#008000");
			$("#is-phone-verified").val("true");
		} else {
			$("#code-msg").text("인증에 실패했습니다.");
			$("#code-msg").css("color", "#FF0000");
			$("#is-phone-verified").val("false");
		}
	})
	.catch(function(error) {
		alert("에러! : " + error);
	});
}