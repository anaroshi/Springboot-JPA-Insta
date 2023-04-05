package com.cos.insta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cos.insta.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer>{	
	
	// 내가 좋아요 선택한 이미지 찾기
	@Transactional(readOnly = true)	
	Likes findByUserIdAndImageId(int userId, int imageId);
	
	// 이미지 좋아요 카운트
	@Transactional(readOnly = true)	
	int countByImageId(int imageId);

}
