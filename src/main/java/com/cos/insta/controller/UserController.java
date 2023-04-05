package com.cos.insta.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cos.insta.model.Image;
import com.cos.insta.model.User;
import com.cos.insta.repository.FollowRepository;
import com.cos.insta.repository.LikesRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.service.MyUserDetail;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class UserController {
	
	@Value("${file.path}")
	private String fileRealPath;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private LikesRepository likesRepository;

	// http://localhost:8060/auth/login
	@GetMapping("/auth/login")
	public String authLogin() {
		log.info(".......... UserController --- Login");
		return "auth/login";
	}
	
	// 회원 가입
	// http://localhost:8060/auth/join
	@GetMapping("/auth/join")
	public String authJoin() {
		log.info(".......... UserController --- Join");
		return "auth/join";
	}
	
	// 회원 가입 처리
	// http://localhost:8060/auth/JoinProc
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

	// profile 화면
	// http://localhost:8060/user/3
	@GetMapping("/user/{id}")
	public String profile(@AuthenticationPrincipal MyUserDetail userDetail, @PathVariable int id, Model model) {		

		/**
		 * id를 통해서 해당 유저를 검색(이미지+유저정보)
		 * 1. imageCount
		 * 2. followCount
		 * 3. followerCount
		 * 4. User 오브젝트 (Image(likeCount) 컬렉션)
		 * 5. followCheck => follow 유무(1:팔로우, 0:언팔로우)
		 */
		
		// 4. 임시(수정요)
		Optional<User> oToUser = userRepository.findById(id);
		User user = oToUser.get();		
		
		// 1. imageCount
		int imageCount = user.getImages().size();
		model.addAttribute("imageCount", imageCount);
		
		// 2. followCount
		// select count(*) from follow where fromUserId=1
		int followCount = followRepository.countByFromUserId(user.getId());
		model.addAttribute("followCount", followCount);
		
		// 3. followerCount
		// select count(*) from follow where toUserId=1
		int followerCount = followRepository.countByToUserId(user.getId());
		model.addAttribute("followerCount", followerCount);		
		
		// 4. likeCount
		// select count(*) from like where imageId=27
		for(Image item:user.getImages()) {
			int likeCount = likesRepository.countByImageId(item.getId());
			item.setLikeCount(likeCount);		
		}
		
		model.addAttribute("user", user);
						
		// 5. followCheck 유무
		User principal = userDetail.getUser();
		int followCheck = followRepository.countByFromUserIdAndToUserId(principal.getId(), id);
		log.info("--------------------- followCheck : "+followCheck);
		
		model.addAttribute("followCheck", followCheck);
		
		return "user/profile";
	}
	
	// http://localhost:8060/user/edit
	@GetMapping("/user/edit")
	public String userProfileEdit(@AuthenticationPrincipal MyUserDetail userDetail, Model model) {
		
		Optional<User> oUser = userRepository.findById(userDetail.getUser().getId());
		User user = oUser.get();
		
		model.addAttribute("user", user);
		
		return "user/profile_edit";
	}
	
	@PostMapping("/user/editProc")
	public String userProfileEditProc(User requestUser, @AuthenticationPrincipal MyUserDetail userDetail) {
		
		// 영속화		
		Optional<User> oUser = userRepository.findById(userDetail.getUser().getId());
		User user = oUser.get();
		
		// 값 변경
		user.setName(requestUser.getName());
		user.setUsername(requestUser.getUsername());
		user.setWebsite(requestUser.getWebsite());
		user.setBio(requestUser.getBio());
		user.setEmail(requestUser.getEmail());
		user.setPhone(requestUser.getPhone());
		user.setGender(requestUser.getGender());
		
		// 다시 영속화 및 flush
		userRepository.save(user);
		
		return "redirect:/user/"+userDetail.getUser().getId();
	}
	
	
	// 프로파일 업로드
	@PostMapping("/user/profileUpload")
	public String userProfileUpload (
		@RequestParam("profileImage") MultipartFile file,
		@AuthenticationPrincipal MyUserDetail userDetail
	) throws IOException {
		User principal = userDetail.getUser();
		
		// 파일 처리
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(fileRealPath + uuidFilename);
		Files.write(filePath, file.getBytes());
		
		// 영속화
		Optional<User> oUser = userRepository.findById(principal.getId());
		User user = oUser.get();
		
		// 값 변경
		user.setProfileImage(uuidFilename);
		
		// 다시 영속화 및 저장
		userRepository.save(user);
		
		return "redirect:/user/"+principal.getId();
	}
	
}
