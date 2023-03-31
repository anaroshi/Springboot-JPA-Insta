package com.cos.insta.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.insta.service.MyUserDetail;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class ImageController {

	@GetMapping({"/", "/image/feed"})
	public String ImageFeed(@AuthenticationPrincipal MyUserDetail userDetail) {
		log.info(".......... ImageController --- ImageFeed username : "+userDetail.getUsername());		
		return "image/feed";
	}
	
}
