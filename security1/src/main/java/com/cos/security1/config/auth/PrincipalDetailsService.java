package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수기 실행됨
// 규칙이기 때문에 무조건 이렇게 만들어줘야함
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session (내부 Authentication (내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if( userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
