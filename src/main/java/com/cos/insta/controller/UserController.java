package com.cos.insta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cos.insta.model.User;
import com.cos.insta.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class UserController {

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;

	// http://localhost:8060/auth/login
	@GetMapping("/auth/login")
	public String authLogin() {
		log.info(".......... UserController --- Login");
		return "auth/login";
	}
	
	// http://localhost:8060/auth/join
	@GetMapping("/auth/join")
	public String authJoin() {
		log.info(".......... UserController --- Join");
		return "auth/join";
	}
	
	@PostMapping("/auth/JoinProc")
	public String authJoinProc(User user) {
		log.info(".......... UserController --- JoinProc user : "+ user);
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword); // 패스워드 암호화
		userRepository.save(user);
		log.info(".......... rawPassword : "+rawPassword+", encPassword : "+encPassword);
		return "redirect:/auto/login";
	}

}
