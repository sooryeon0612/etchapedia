package com.etchapedia.user;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsersService {
	@Autowired
	private UsersRepository uRepo;
	@Autowired
	private PasswordEncoder pwEncoder;
	
	@Value("${file.upload-dir}")
    private String myDir;
	
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
	
	// 비밀번호 일치 여부
	public boolean checkUserByPw(Integer userIdx, String password) {
		Optional<Users> ou = uRepo.findById(userIdx);
		Users u = ou.get();
		if(pwEncoder.matches(password, u.getPassword())) return true;
		return false;
	}
	
	// 비밀번호 변경 
	@Transactional
	public void changePw(Integer userIdx, String password) {
		Optional<Users> ou = uRepo.findById(userIdx);
		Users u = ou.get();
		u.setPassword(pwEncoder.encode(password));	
	}
	
	// 프로필 이미지 저장 
	@Transactional
	public void saveProfile(MultipartFile img, Integer userIdx) throws IllegalStateException, IOException {
		String originFilename = img.getOriginalFilename();
        String fileExtension = originFilename.substring(originFilename.lastIndexOf("."));
        String saveFileName = UUID.randomUUID().toString() + fileExtension;
        
        File uploadDir = new File(myDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File dest = new File(uploadDir, saveFileName);
        
        img.transferTo(dest);
		Optional<Users> ou = uRepo.findById(userIdx);
		if(ou.isPresent()) {
			Users u = ou.get();
			u.setProfile(saveFileName);
		}
	}
	
}
