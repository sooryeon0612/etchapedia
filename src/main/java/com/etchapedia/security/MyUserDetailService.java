package com.etchapedia.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.etchapedia.user.Users;
import com.etchapedia.user.UsersRepository;

@Service
public class MyUserDetailService implements UserDetailsService {
	@Autowired
	private UsersRepository uRepo;

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Users> om = uRepo.findByEmail(email);
		if(om.isEmpty())
			throw new UsernameNotFoundException("사용자를 찾을 수 없음.");
		
		Users m = om.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if("admin" .equals(email))
			authorities.add(new SimpleGrantedAuthority(UsersRole.ADMIN.getValue()));
		else
			authorities.add(new SimpleGrantedAuthority(UsersRole.USER.getValue()));
		
		return new CustomUserDetails(m.getEmail(), m.getPassword(), true, true, true, true, authorities, m.getUserIdx(), m.getName(), m.getProfile());
	}
}
