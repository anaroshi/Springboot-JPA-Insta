package com.cos.insta.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.insta.model.Follow;
import com.cos.insta.model.User;
import com.cos.insta.repository.FollowRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.service.MyUserDetail;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class FollowController {

	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// Follow 처리
	// http://localhost:8060/follow/3
	@PostMapping("/follow/{id}")
	public @ResponseBody String follow(@AuthenticationPrincipal MyUserDetail userDetail, @PathVariable int id) {
		log.info(".... FollowController -- follow id : "+id+", userDetail : "+userDetail); 
		
		// 로그인 회원 정보
		User fromUser = userDetail.getUser();
		
		// Follow 대상자 정보
		Optional<User> oToUser = userRepository.findById(id);
		User toUser = oToUser.get();
		
		// Follow 대상자 정보
		Follow follow = new Follow();
		follow.setFromUser(fromUser);
		follow.setToUser(toUser);
		
		// DB에 Follow 정보 저장
		followRepository.save(follow);
		
		return "OK";		
	}

	// unFollow 처리
	// http://localhost:8060/follow/3
	@DeleteMapping("/follow/{id}")
	public @ResponseBody String unFollow(@AuthenticationPrincipal MyUserDetail userDetail, @PathVariable int id) {
		log.info(".... FollowController -- unFollow id : "+id+", userDetail : "+userDetail); 

		// 로그인 회원 정보
		User fromUser = userDetail.getUser();
		
		// Follow 대상자 정보
		Optional<User> oToUser = userRepository.findById(id);
		User toUser = oToUser.get();
		
		// DB에서 Follow 정보 삭제
		int result = followRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
		log.info("result : "+result);
		
		return "OK";
	}
	
	@GetMapping("/follow/follower/{id}")
	public String followFollower(@PathVariable int id) {
		
		// 팔로워 리스트
		
		return "follow/follow";
	}
	
	@GetMapping("/follow/follow/{id}")
	public String followFollow(@PathVariable int id) {
		
		// 팔로우 리스트
		
		return "follow/follow";
	}	
}
