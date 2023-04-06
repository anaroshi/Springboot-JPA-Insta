package com.cos.insta.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.insta.model.User;
import com.cos.insta.model.dto.KakaoProfile;
import com.cos.insta.model.dto.OAuth2Token;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class OAuth2Controller {
	
	private String clientId = "b353b3e01d0a3b17503c48af3fb63cf0";
	private String redirectUri = "http://localhost:8060/auth/kakao/callback";
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
		
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/auth/kakao/login")
	public String kakaoLogin() {
		StringBuffer sb = new StringBuffer();
		sb.append("https://kauth.kakao.com/oauth/authorize?");			
		sb.append("client_id="+clientId+"&");
		sb.append("redirect_uri="+redirectUri+"&");
		sb.append("response_type=code");
		
		log.info("kakaoLogin() : "+sb.toString());
		
		return "redirect:"+sb.toString();
	}
	
	@PostMapping("/auth/kakao/joinProc")
	public String kakaoJoinProc(User user, HttpSession session) {
		log.info("kakaoJoinProc() : "+user);		
		
		// name, email, providerId
		String providerId = (String) session.getAttribute("providerId");
		user.setProvider("kakao");
		user.setProviderId(providerId);
		
		// name & password는 필수 입력 항목이어서 값을 넣어줌		
		user.setName(user.getUsername());
		user.setPassword(encoder.encode(cosKey)); // 비밀번호 해쉬 처리
		userRepository.save(user);
		
		UserDetails userDetail = myUserDetailsService.loadUserByUsername(user.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		
		return "redirect:/";
	}
	
	// accessToken 역활 == 카카오에 정보를 받기 위한 key(10분~1시간)
	// refreshToken -> accessToken 을 받을 수 있음.(10일 ~ 30일)	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code, HttpSession session) {
		
		// 토큰 받기
		// HttpUrlConnection, Retrofit2, okHttp, restTemplate
		// Post방식으로 key=value 데이터를 요청(카카오쪽으로)
		RestTemplate rt = new RestTemplate();
		
		// HttpHeaders 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded; charset=utf-8"); // key=value 형태의 데이터임을 알림
		
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("grant_type", "authorization_code");
		parameters.add("client_id", clientId);
		parameters.add("redirect_uri", redirectUri);
		parameters.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
		
		ResponseEntity response = rt.exchange(
				"https://kauth.kakao.com/oauth/token", 
				HttpMethod.POST, 
				request, 
				String.class
		);
		
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		
		OAuth2Token oToken = null;
		try {
			oToken = objectMapper.readValue(response.getBody().toString(), OAuth2Token.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 Access token : "+ oToken.getAccess_token());
		// "카카오 로그인 토큰 요청 완료 - 토큰요청에 대한 응답 : "+response.getBody();
		
		// 회원 프로필 조회 끝(인증)
		// Post방식으로 key=value 데이터를 요청(카카오쪽으로)
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeaders 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oToken.getAccess_token()); // 전부다 String형일 때. RestTemplate 때문에 생략 가능
		headers2.add("Content-type", "application/x-www-form-urlencoded; charset=UTF-8"); // key=value 형태의 데이터임을 알림
		
		// HttpHeaders와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity request2 = new HttpEntity(headers2);
		
		// Http 요청하기 - POST방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me", 
				HttpMethod.POST, 
				request2, 
				String.class
		);
		
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;		
		try {
			log.info("response2.getBody().toString() : "+response2.getBody().toString());
			log.info("KakaoProfile.class : "+KakaoProfile.class);
			kakaoProfile = objectMapper2.readValue(response2.getBody().toString(), KakaoProfile.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("KakaoProfile Id : "+ kakaoProfile.getId());

		// 가입자, 비가입자 확인 처리
		User user = userRepository.findByProviderAndProviderId("kakao", kakaoProfile.getId());
		
		if(user==null) {
			log.info("미 가입자입니다.");
			
			// 회원가입 창으로 넘어가서 email, name -> 로그인 처리
			session.setAttribute("providerId", kakaoProfile.getId());
			return "auth/kakaoJoin";
		} else {
			// 로그인 처리
			// 해당 아이디로 로그인을 위해 강제 세션 부여
			UserDetails userDetail = myUserDetailsService.loadUserByUsername(user.getUsername());
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
			
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
			return "redirect:/";
		}
	}
	
}