package com.cos.insta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.insta.model.User;
import com.cos.insta.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		
		MyUserDetail userDetail = null;
		if (user != null) {		
			userDetail = new MyUserDetail();
			userDetail.setUser(user);
		} else {
			throw new UsernameNotFoundException("Not Found 'username'");
		}
		log.info("MyUserDetailsService ................... userDetail : "+userDetail);
		return userDetail;
	}	
}
