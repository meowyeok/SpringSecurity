package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다!!
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String index(){
        // 머스테치 기본폴더 : src/main/resources/
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) 로잡으면 설정끝 (자동이라 생략가능)
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole("ROLE_USER"); // 이 필드는 직접 입력해줌
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); // 암호화된 패스워드로 세팅
        userRepository.save(user); // 회원가입 잘됨. 근데 비밀번호 1234 평문 => 시큐리티로 로그인 불가능, 패스워드가 암호화가 안되어서
        return "redirect:/loginForm"; // 리다이렉트를 붙이면 해당 함수를 호출함
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLD_MANAGER') or hasRole('ROLE_ADMIN')") // 해당 함수가 실행되기 직전에 실행됨
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }

//    @PreAuthorize("#post.user.id == principal.user.id")
//    @GetMapping("/individual")
//    public @ResponseBody String individual(){
//        return "individual";
//    }
}
