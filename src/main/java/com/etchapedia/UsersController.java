package com.etchapedia;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import com.etchapedia.api.KakaoAuthClient;
import com.etchapedia.api.KakaoResourceClient;
import com.etchapedia.security.CustomUserDetails;
import com.etchapedia.security.MyUserDetailService;
import com.etchapedia.user.Users;
import com.etchapedia.user.UsersCreateForm;
import com.etchapedia.user.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UsersController {
	@Autowired
	private UsersService uSvc;
	@Autowired
	private MyUserDetailService mSvc;
	@Autowired
	private KakaoAuthClient kakaoA;
	@Autowired
	private KakaoResourceClient kakaoR;
	
	// 작업자 : 이경미
	// 회원가입
	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("userCreateForm", new UsersCreateForm());
		return "signup";
	}
	
	// 작업자 : 이경미
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
		return "redirect:/user/login";
	}
    
    // 작업자 : 이경미	
    // 이메일 중복 여부 확인
    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam("email") String UserEmail) {
    	boolean isDuplicated = uSvc.isDuplicated(UserEmail);
    	Map<String, Boolean> response = new HashMap<>();
    	response.put("isDuplicated", isDuplicated);
    	return response;
    }
    
    // 작업자 : 서수련
    // 기능 : 사용자 이름 변경
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
    
    // 작업자 : 서수련 
    // 기능 : 비밀번호 변경 
    @PostMapping("/password")
    public String updateName(@AuthenticationPrincipal CustomUserDetails userDetails,
    						 @RequestParam("new-password")String newPw,
    						 @RequestParam("current-password")String curPw) {
    	Integer userIdx = userDetails.getUserIdx();
    	if(!uSvc.checkUserByPw(userIdx, curPw)) return "redirect:/mypage?error";
    	uSvc.changePw(userIdx, newPw);
    	return "redirect:/user/logout";
    }
    
    // 작업자 : 서수련 
    // 기능 : 프로필 사진 변경
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
    
    // 작업자 : 서수련 
    // 기능 : 카카오 소셜 로그인
    @GetMapping("/login/kakao")
    public String getAuthCode(@RequestParam("code")String code, HttpSession session) throws JsonMappingException, JsonProcessingException {
    	String accessToken = uSvc.getAccessToken(kakaoA.getRawAccessToken(code));
    	String info = kakaoR.callKakaoResource(accessToken);
    	Users rawUser = uSvc.getUserInfoFromKakao(info);
    	Optional<Users> ou = uSvc.findByEmail(rawUser.getEmail());
    	Users saveUser;
    	if(ou.isEmpty()) { saveUser = uSvc.insertKakaoUser(rawUser); } 
    	else { saveUser = ou.get(); }
    	CustomUserDetails kakaoUser = mSvc.loadUserByUsername(saveUser.getEmail());
    	Authentication newAuth =
                new UsernamePasswordAuthenticationToken(
                		kakaoUser,
                		null,
                		kakaoUser.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        session.setAttribute(
                "SPRING_SECURITY_CONTEXT", 
                SecurityContextHolder.getContext()
        );
    	return "redirect:/home";
    }
    
    
}
