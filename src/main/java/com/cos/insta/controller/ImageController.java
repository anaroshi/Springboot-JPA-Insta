package com.cos.insta.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cos.insta.model.Image;
import com.cos.insta.model.Tag;
import com.cos.insta.model.User;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.TagRepository;

import com.cos.insta.service.MyUserDetail;
import com.cos.insta.util.Utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class ImageController {
	
	@Value("${file.path}")
	private String fileRealPath;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private TagRepository tagRepository;

	// http://localhost:8060/image/feed
	@GetMapping({"/", "/image/feed"})
	public String ImageFeed(@AuthenticationPrincipal MyUserDetail userDetail) {
		log.info(".......... ImageController --- ImageFeed username : "+userDetail.getUsername());		
		return "image/feed";
	}
	
	// http://localhost:8060/image/upload
	@GetMapping("/image/upload")
	public String imageUpload() {
		return "image/image_upload";
	}
	
	// http://localhost:8060/image/uploadProc
	@PostMapping("/image/uploadProc")
	public String imageUploadProc(
			@AuthenticationPrincipal MyUserDetail userDetail, 
			@RequestParam("file") MultipartFile file,
			@RequestParam("caption") String caption,
			@RequestParam("location") String location,
			@RequestParam("tags") String tags			
	) {
//		log.info("userDetail : "+userDetail);
//		log.info("file :  "+file+", caption : "+caption+", location : "+location+", tags : "+tags);
		
		// 이미지 업로드 수행
		UUID uuid = UUID.randomUUID();
		String uuidFileName = uuid+"_"+file.getOriginalFilename();
		
		Path filePath = Paths.get(fileRealPath+uuidFileName);
		// log.info("2......filePath : "+filePath);
		
		try {
			Files.write(filePath, file.getBytes()); // 하드 디스크에 기록(filePath에 이미지 저장)
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		User principal = userDetail.getUser();
		
		Image image = new Image();
		image.setLocation(location);
		image.setCaption(caption);
		image.setUser(principal);
		image.setPostImage(uuidFileName);
		
		// <img src="/upload/파일명" />		

		imageRepository.save(image);

		// Tag 객체 생성 집어 넣음.
		List<String> tagList = Utils.tagParser(tags);
		// log.info("5......tagList : "+tagList);
		
		for(String tag : tagList) {
			Tag t = new Tag();
			t.setName(tag);
			t.setImage(image);
			tagRepository.save(t);
			image.getTags().add(t);
		}
		
		// log.info("image tags"+image.getTags());
		
		return "redirect:/";
	}
	
}
