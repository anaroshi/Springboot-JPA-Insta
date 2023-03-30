package com.cos.insta.config;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 빈등록 (IoC관리)
@EnableWebSecurity // 시큐리티 필터가 등록이 된다.
//Controller에서 특정 권한이 있는 유저만 접근을 허용하려면 @PreAuthorize 어노테이션을 사용하는데, 해당 어노테이션을 활성화 시키는 어노테이션이다.
public class SecurityConfig {

//	@Autowired
//	private UserDetailsService userDetailsService;
	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
	
	// 비밀번호를 해쉬화하여 암호화한다.
	@Bean
	public BCryptPasswordEncoder encodePWD() {		
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알야야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.	
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		System.out.println(".... SecurityConfig - configure auth  : "+auth);
//		auth.userDetailsService(userDetailsService).passwordEncoder(encodePWD());
//	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 게 좋음)
			.cors().disable()
			.authorizeHttpRequests()
	        .antMatchers("/", "/user/**", "/follow/**", "/auth/**", "/js/**", "/css/**", "/images/**", "/dummy/**")
	        .permitAll()
            .anyRequest()
            .authenticated()            
		.and()
			.formLogin()
			.loginPage("/auth/login") // 이동하는 로그인 화면은 단일로 지정하기 /auth/loginForm 과 같이 auth가 더 있으면 인식하지 못함.
			.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 해준다.
			.defaultSuccessUrl("/image/feed"); // 로그인이 정상으로 끝나면 가는 위치
//			.failureUrl("/fail"); // 로그인 실패시 가는 위치
	
		return http.build();
	}

}