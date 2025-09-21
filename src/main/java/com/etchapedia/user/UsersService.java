package com.etchapedia.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
	@Autowired
	private UsersRepository uRepo;
	@Autowired
	private PasswordEncoder pwEncoder;
	
	// 회원가입.
	public Users create(String name, String email, String pw) {
		Users u = new Users();
		u.setName(name);
		u.setEmail(email);
		u.setPassword(pwEncoder.encode(pw));
		uRepo.save(u);
		return u;
	}
	
	// 중복여부 체크.
	public boolean isDuplicated(String email) {
		Optional<Users> ou = uRepo.findByEmail(email);
		return ou.isPresent();
	}
	
	// 사용자 찾기
	public boolean authenticate(String email, String pw) {
		Optional<Users> userOpt = uRepo.findByEmail(email);
		
		if(userOpt.isPresent()) {
			Users user = userOpt.get();
			if(user.getPassword().equals(pw)) {
				return true;
			}
		}
		return false;
	}
	
	// 이름 변경
	@Transactional
	public void updateName(Integer userIdx, String name) {
		Optional<Users> ou = uRepo.findById(userIdx);
		Users u = ou.get();
		u.setName(name);
	}
}
