package com.cos.insta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cos.insta.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer>{	
	
	// 내가 좋아요 선택한 이미지 찾기
	@Transactional(readOnly = true)	
	Likes findByUserIdAndImageId(int userId, int imageId);
	
	// 이미지 좋아요 카운트
	@Transactional(readOnly = true)	
	int countByImageId(int imageId);
	
	@Transactional(readOnly = true)	
	@Query(value = "SELECT * FROM likes WHERE imageId in (SELECT id FROM image WHERE userId = ?1) order by id desc limit 5;", nativeQuery = true)
	List<Likes> findLikeNotification(int userId);

}
