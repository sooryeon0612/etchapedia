package com.etchapedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.etchapedia.user.UsersCreateForm;
import com.etchapedia.user.UsersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UsersController {
	@Autowired
	private UsersService uSvc;
	
	@GetMapping("/signup")
	public String signupForm(UsersCreateForm userCreateForm) {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid UsersCreateForm userCreateForm, BindingResult br) {
		if(br.hasErrors()) {
			return "signup_form";
		}
		
		String email = userCreateForm.getUserEmail();
		if(uSvc.isDuplicated(email)) {
			br.rejectValue("userEmail", "duplicatedId중x", "이미 가입된 EMAIL임.");
			return "signup_form";
		}
		
		String name = userCreateForm.getUserName();
		String pw = userCreateForm.getUserPw();
		uSvc.create(name, email, pw);
		return "redirect:/";
	}
}
