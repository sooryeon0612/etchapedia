package com.etchapedia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration      // 스프링의 환경설정 클래스임을 의미.
@EnableWebSecurity   // 스프링 시큐리티 활성화
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 예외 처리
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                new AntPathRequestMatcher("/order/pay/**"),        // 카카오페이 결제 관련
                new AntPathRequestMatcher("/cart/add"),            // 장바구니 추가
                new AntPathRequestMatcher("/cart/update-quantity"),// 장바구니 수량 변경
                new AntPathRequestMatcher("/cart/order")           // 장바구니 주문
            ))
            // 인가 규칙 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/user/login/kakao")).permitAll()  // 카카오 로그인 허용
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()                // 기본적으로 모두 허용
            )
            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
            )
            // 로그아웃 설정
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/home")
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
}

