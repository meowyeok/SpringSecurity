package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 기본 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화 / preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated() // /인증만 되면 들어갈 수 있는 주소
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") // MANAGER 또는 ADMIN 권한 필요
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한만 접근 가능
                        .anyRequest().permitAll() // 나머지 요청은 모두 허용
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginForm").permitAll() // 커스텀 로그인 페이지
                        .loginProcessingUrl("/login") // 로그인 폼의 action 경로
                        .defaultSuccessUrl("/") // 로그인 성공 시 리디렉션될 기본 URL,
                );

        return http.build();
    }
}