package com.etchapedia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
<<<<<<< HEAD
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration		// 스프링의 환경설정 클래스임을 의미.
@EnableWebSecurity	// 스프링 시큐리티 활성화
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http		// HttpSecurity객체 : Spring Security 의 설정 역할.
		.authorizeHttpRequests(		// HTTP 요청에 대한 인가(Authorization) 설정을 시작.
			(authorizeHttpRequests) -> authorizeHttpRequests		
				.requestMatchers(new AntPathRequestMatcher("/**"))	// 어떤 요청 경로에 대해 규칙을 적용
				.permitAll()		// 해당 경로에 대한 접근을 모든 사용자에게 허용.
		)
		.formLogin(		// 로그인 페이지에 대한 설정을 시작.
				(formLoginConfig) -> formLoginConfig
				.loginPage("/user/login")		// 스프링 시큐리티에게 로그인페이지URL을 알려줌.
												// ---> 그러면 만약에 인증이 필요한 요청에 인증X 사용자가 접근하면,
												// 		/member/login 으로 리다이렉트 시켜줌.
				.usernameParameter("userEmail")	// <form>에서 ID 파라미터의 이름 설정. ("username"이면 불필요)
				.passwordParameter("userPw")	// <form>에서 PW 파라미터의 이름 설정. ("password"이면 불필요)
				.defaultSuccessUrl("/user/signup")		// 로그인 성공 시, redirect 할 URL.
		)
		.logout(
				(logoutConfig) -> logoutConfig
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.invalidateHttpSession(true)			// 세션무효화
				.logoutSuccessUrl("/user/login")		// 로그아웃 성공 시, redirect할 URL.
		);
		return http.build();
	}
	
	@Bean
	PasswordEncoder pwEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
=======
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http // httpSecurity 객체 : Spring Security의 설정 역할.
		.authorizeHttpRequests( // HTTP요청에 의한 인가(Authorization) 설정을 시작할게
				(authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers(new AntPathRequestMatcher("/**")) // 어떤 경로에 대해 규칙을 적용
					.permitAll() // 해당 경로에 대한 접근을 모든 사용자에게 허용
		)
		.formLogin( // 로그인 페이지에 대한 설정을 시작할게 
			(formLoginConfig) -> formLoginConfig
				.loginPage("/user/login") // 스프링 시큐리티에게 로그인 페이지 알려줌. 인증필수인 페이지에 인증되지 않은 사용자가 접근하면 /user/login으로 리다이렉트 시켜줌
				.loginProcessingUrl("/user/login")
				.usernameParameter("email") // form에서 ID파라미터의 이름 설정. ("username"으로 했다면 불필요)
				.passwordParameter("pw") // form에서 PW파라미터의 이름 설정. ("userpassword"으로 했다면 불필요)
				.defaultSuccessUrl("/hello") // 로그인 성공 시, redirect할 URL.
		)
		.logout(
			(logoutConfig) -> logoutConfig
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.invalidateHttpSession(true) // 세션 무효화 
				.logoutSuccessUrl("/hello") // 로그아웃 성공 시, redirect할 URL.
		);
		return http.build();
	}
	
	@Bean
	PasswordEncoder pwEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

>>>>>>> refs/heads/testSR
}
