package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


// 시큐리티가 /login 주소 요청을 낚아채서 로그인을 진행시킨다.
// 로그인이 완료가 되면 시큐리티 session을 만들어준다. (Security ContextHolder)라는 키값에다가 세션정보 저장
// 여기 들어갈 수 있는 오브젝트가 정해져있음 => Authentication 타입 객체
// Authentication 안에는 User 정보가 있어야 됨.
// 이때도 클래스가 정해져 있음, User 오브젝트 타입 => UserDetails 타입 객체

//즉, Security Session => Authentication => UserDetails

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user){
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> collect = new ArrayList<>(); // 리턴타입을 맞춰준다.
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }


    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){

        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로 함.
        // 현재시간 - 로긴시간 -> 1년 초과하면 return false 처럼 사용

        return true;
    }

    public User getUser() {
        return this.user;
    }

}
