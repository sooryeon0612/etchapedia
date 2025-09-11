package com.etchapedia;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UsersController {
//	@Autowired
//	private UsersService uSvc;
//	
//	@GetMapping("/signup")
//	public String signupForm(UserCreateForm userCreateForm) {
//		return "signup_form";
//	}
//	
//	@PostMapping("/signup")
//	public String signup(@Valid UserCreateForm userCreateForm, BindingResult br) {
//		if(br.hasErrors()) {
//			return "signup_form";
//		}
//		
//		String email = userCreateForm.getUserEmail();
//		if(uSvc.isDuplicated(email)) {
//			br.rejectValue("userEmail", "duplicatedId중x", "이미 가입된 EMAIL임.");
//			return "signup_form";
//		}
//		
//		String name = userCreateForm.getUserName();
//		String pw = userCreateForm.getUserPw();
//		uSvc.create(name, email, pw);
//		return "redirect:/";
//	}
}
