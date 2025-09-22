package com.etchapedia;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.etchapedia.security.CustomUserDetails;
import com.etchapedia.security.MyUserDetailService;
import com.etchapedia.user.UsersCreateForm;
import com.etchapedia.user.UsersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UsersController {
	@Autowired
	private UsersService uSvc;
	@Autowired
	private MyUserDetailService mSvc;
	
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
    @GetMapping("/login_form")
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
    
    // 이름 변경
    @PostMapping("/name")
    public String updateName(@AuthenticationPrincipal CustomUserDetails userDetails,
    						 @RequestParam("name")String name) {
    	Integer userIdx = userDetails.getUserIdx();
    	uSvc.updateName(userIdx, name);
    	CustomUserDetails updateUser = mSvc.loadUserByUsername(userDetails.getUsername());
    	
    	Authentication newAuth =
                new UsernamePasswordAuthenticationToken(
                		updateUser,
                		updateUser.getPassword(),
                		updateUser.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    	return "redirect:/mypage";
    }
    
    // 비밀번호 변경 
    @PostMapping("/password")
    public String updateName(@AuthenticationPrincipal CustomUserDetails userDetails,
    						 @RequestParam("new-password")String newPw,
    						 @RequestParam("current-password")String curPw) {
    	Integer userIdx = userDetails.getUserIdx();
    	if(!uSvc.checkUserByPw(userIdx, curPw)) return "redirect:/mypage?error";
    	uSvc.changePw(userIdx, newPw);
    	return "redirect:/user/logout";
    }
    
    // 프로필 사진 변경
    @PostMapping("/profile")
    public String changeProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
    							@RequestParam("file")MultipartFile img) throws IllegalStateException, IOException {
    	Integer userIdx = userDetails.getUserIdx();
    	uSvc.saveProfile(img, userIdx);
    	CustomUserDetails updateUser = mSvc.loadUserByUsername(userDetails.getUsername());
    	Authentication newAuth =
                new UsernamePasswordAuthenticationToken(
                		updateUser,
                		updateUser.getPassword(),
                		updateUser.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    	return "redirect:/mypage";
    }
}
