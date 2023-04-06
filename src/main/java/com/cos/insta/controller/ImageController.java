package com.cos.insta.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cos.insta.model.Image;
import com.cos.insta.model.Likes;
import com.cos.insta.model.Tag;
import com.cos.insta.model.User;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.LikesRepository;
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
	
	@Autowired
	private LikesRepository likesRepository;
	
	// 좋아요
	//  http://localhost:8060/image/like/$%7BimageId%7D 
	@PostMapping("/image/like/{id}")
	public @ResponseBody String imageLike(
			@PathVariable int id,
			@AuthenticationPrincipal MyUserDetail userDetail
	) {
		log.info("----------------------imageLike : "+id);
		Likes oldLike = likesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), id);
		Optional<Image> oImage = imageRepository.findById(id);
		Image image = oImage.get();

		try {
			if(oldLike==null) { // '좋아요' 안한 상태(추가)
				Likes newLike = Likes.builder().image(image).user(userDetail.getUser()).build();
				likesRepository.save(newLike);
				return "like";
			} else { // '좋아요' 한 상태(삭제)
				likesRepository.delete(oldLike);
				return "unLike";
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "fail";
	}
	

	// http://localhost:8060/image/feed
	@GetMapping({"/", "/image/feed"})
	public String ImageFeed(
			@AuthenticationPrincipal MyUserDetail userDetail, 
			@PageableDefault(size = 3, sort = "id", direction = Direction.DESC) Pageable pageable,
			Model model
	) {
		log.info(".......... ImageController --- ImageFeed userDetail : "+userDetail);
		
		// 1. 내가 follow한 친구들의 사진
		Page<Image> pageImages = imageRepository.findImage(userDetail.getUser().getId(), pageable);
		List<Image> images = pageImages.getContent();
		for(Image image:images) {
			Likes like = likesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if(like != null) {
				image.setHeart(true);
			}
		}
		model.addAttribute("images", images);
		
		return "image/feed";
	}	
	
	// http://localhost:8060/image/feed/scroll?page=1..2..3..4
	@GetMapping("image/feed/scroll")
	public @ResponseBody List<Image> imageFeedScroll(@AuthenticationPrincipal MyUserDetail userDetail,
			@PageableDefault(size = 3, sort = "id", direction = Direction.DESC) Pageable pageable
	){
		Page<Image> pageImages = imageRepository.findImage(userDetail.getUser().getId(), pageable);
		List<Image> images = pageImages.getContent();
		
		for(Image image:images) {
			Likes like = likesRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if(like != null) {
				image.setHeart(true);
			}
		}		
		
		return images;
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
	) throws IOException {
//		log.info("userDetail : "+userDetail);
//		log.info("file :  "+file+", caption : "+caption+", location : "+location+", tags : "+tags);
		
		// 이미지 업로드 수행
		UUID uuid = UUID.randomUUID();
		String uuidFileName = uuid+"_"+file.getOriginalFilename();
		
		Path filePath = Paths.get(fileRealPath+uuidFileName);
		// log.info("2......filePath : "+filePath);
		
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}

		
		// 이미지 동기처리 하기(용량이 큰 이미지 일경우)  --- 시작
//		Files.write(filePath, file.getBytes()); // 하드 디스크에 기록(filePath에 이미지 저장)
	
		AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.WRITE);
		
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		buffer.put(file.getBytes());
		buffer.flip();
		
		Future<Integer> operation = fileChannel.write(buffer, 0);
		buffer.clear();
		
		while(!operation.isDone());
		// 이미지 동기처리 하기(용량이 큰 이미지 일경우)  --- 끝		
		
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
	
	@GetMapping("image/explore")
	public String imageExplore(Model model, @PageableDefault(size = 9, sort = "id", direction = Direction.DESC) Pageable pageable) {
		log.info("...............explore");
		
		Page<Image> pageImages = imageRepository.findAll(pageable);
		List<Image> images = pageImages.getContent();
		
		for(Image image:images) {
			int likeCount = likesRepository.countByImageId(image.getId());
			image.setLikeCount(likeCount);
		}		
		
		model.addAttribute("images", images);
		return "image/explore";
	}

}
