package com.etchapedia.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private UsersRepository uRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Users> om = uRepo.findByEmail(email);
		if(om.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없음.");
		}
		Users m = om.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		// authorities : 사용자가 가진 권한 목록( ex. ROLE_ADMIN, ROLE_USER)
		
//		관리자 권한이 존재할 때 
//		if("admin".equals(username))
//			authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
//		else
			
		authorities.add(new SimpleGrantedAuthority(UsersRole.USER.getValue()));
		
		return new User(m.getEmail(), m.getPassword(), authorities);
	}


	
}
