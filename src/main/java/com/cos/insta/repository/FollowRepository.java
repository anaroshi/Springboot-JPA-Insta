package com.cos.insta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cos.insta.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
	
	// unFollow
	@Transactional
	int deleteByFromUserIdAndToUserId(int fromUserId, int toUserId);
	
	// Follow 여부 확인
	@Transactional(readOnly = true)
	//@Query(value = "SELECT count(*) FROM follow WHERE fromUserId=?1 AND toUserId=?2")
	int countByFromUserIdAndToUserId(int fromUserId, int toUserId);
	
	// 팔로우 리스트 (하얀 버튼)
	List<Follow> findByFromUserId(int fromUserId);
	
	// 팔로워 리스트 ( 맞팔 체크 후 버튼 색깔 결정)
	List<Follow> findByToUserId(int toUserId);
	
	// 팔로우 카운트
	int countByFromUserId(int fromUserId);
	
	// 팔로워 카운트
	int countByToUserId(int toUserId);
}
