package com.cos.insta.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.insta.model.Follow;
import com.cos.insta.model.Image;
import com.cos.insta.model.Likes;
import com.cos.insta.model.Tag;
import com.cos.insta.model.User;

@Controller
public class TestController {
	
	// http://localhost:8060/test/home
	@GetMapping("/test/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/test/user")
	public @ResponseBody User user() {
		User user = User.builder().id(1).username("cos").name("홍길동").email("cos@nate.com").profileImage("my.jpg").build();
		Image image1 = Image.builder().id(1).caption("음식 사진").postImage("food.jpg").location("부산 서면").user(user).build();
		Image image2 = Image.builder().id(2).caption("장난감 사진").postImage("game.jpg").location("서울 강남").user(user).build();
		List<Image> images = new ArrayList<>();
		images.add(image1);
		images.add(image2);
		user.setImages(images);
		return user;
	}
	
	@GetMapping("/test/image")
	public @ResponseBody Image image() {
		Image image = Image.builder().id(1).caption("음식 사진").postImage("food.jpg").location("부산 서면").build();
		User user = User.builder().id(1).username("cos").name("홍길동").email("cos@nate.com").profileImage("my.jpg").build();
		image.setUser(user);
		return image;
	}
	
	@GetMapping("/test/images")
	public @ResponseBody List<Image> images() {
		User user = new User();
		Image image1 = Image.builder().id(1).caption("벛꽃 사진").postImage("flower.jpg").location("서울 여의도").user(user).build();
		Image image2 = Image.builder().id(2).caption("장난감 사진").postImage("game.jpg").location("서울 강남").user(user).build();
		List<Image> images = new ArrayList<>();
		images.add(image1);
		images.add(image2);
		user.setImages(images);		
		return images;
	}
	
	@GetMapping("/test/tag")
	public @ResponseBody Tag tag() {
		Tag tag = Tag.builder().id(1).name("홍길동").build();
		return tag;
	}
	
	@GetMapping("/test/like")
	public @ResponseBody Likes getLike() {
		User user = User.builder().id(1).username("cos").name("홍길동").email("cos@nate.com").profileImage("my.jpg").build();
		Image img1 = Image.builder().id(1).caption("음식 사진").postImage("food.jpg").location("부산 서면").user(user).build();
		Likes like = Likes.builder().id(1).user(user).image(img1).build();
		return like;
	}

	@GetMapping("/test/follows")
	public @ResponseBody List<Follow> getFollows() {
		
		User user1 = User.builder().id(1).username("cos").name("홍길동").email("cos@nate.com").profileImage("my.jpg").build();
		User user2 = User.builder().id(2).username("sundor").name("성정화").email("sundor@hanmail.net").profileImage("sundor.jpg").build();
		User user3 = User.builder().id(3).username("love").name("장보고").email("love@nate.com").profileImage("love.jpg").build();
		
		Follow follow1 = Follow.builder().id(1).fromUser(user1).toUser(user2).build();
		Follow follow2 = Follow.builder().id(2).fromUser(user1).toUser(user3).build();
		Follow follow3 = Follow.builder().id(3).fromUser(user2).toUser(user1).build();
		
		List<Follow> follows = new ArrayList<>();
		follows.add(follow1);
		follows.add(follow2);
		follows.add(follow3);
		
		return follows;
	}	
	
	// http://localhost:8060/test/login
	@GetMapping("/test/login")
	public String testLogin() {
		return "auth/login";
	}
	
	// http://localhost:8060/test/login
	@GetMapping("/test/join")
	public String testJoin() {
		return "auth/join";
	}
	
	// http://localhost:8060/test/profile
	@GetMapping("/test/profile")
	public String testProfile() {
		return "user/profile";
	}
	
	// http://localhost:8060/test/profileEdit
	@GetMapping("/test/profileEdit")
	public String testProfileEdit() {
		return "user/profile_edit";
	}
	
	// http://localhost:8060/test/feed
	@GetMapping("/test/feed")
	public String testFeed() {
		return "image/feed";
	}
	
	// http://localhost:8060/test/imageUpload
	@GetMapping("/test/imageUpload")
	public String testImageUpload() {
		return "image/image_upload";
	}
	
	// http://localhost:8060/test/explore
	@GetMapping("/test/explore")
	public String testExplore() {
		return "image/explore";
	}

}
