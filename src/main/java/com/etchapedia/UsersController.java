package com.etchapedia;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etchapedia.user.UsersCreateForm;
import com.etchapedia.user.UsersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UsersController {
	@Autowired
	private UsersService uSvc;
	
	// 회원가입
	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("userCreateForm", new UsersCreateForm());
		return "signup";
	}
	
	// 회원가입
	@PostMapping("/signup")
	public String signup(@Valid UsersCreateForm userCreateForm, BindingResult br) {
		if(br.hasErrors()) {
			return "signup";
		}
		
		String email = userCreateForm.getUserEmail();
		if(uSvc.isDuplicated(email)) {
			br.rejectValue("userEmail", "duplicatedId중x", "이미 가입된 EMAIL임.");
			return "signup";
		}
		
		String name = userCreateForm.getUserName();
		String pw = userCreateForm.getUserPw();
		uSvc.create(name, email, pw);
		return "redirect:/user/login_form";
	}
	
    // 로그인 화면 (
    @GetMapping("/login")
	public String login() {
		return "login_form";
	}
    
    // 이메일 중복 여부 확인
    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam("email") String UserEmail) {
    	boolean isDuplicated = uSvc.isDuplicated(UserEmail);
    	Map<String, Boolean> response = new HashMap<>();
    	response.put("isDuplicated", isDuplicated);
    	return response;
    }
}
