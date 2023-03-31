package com.cos.insta.repository;

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
	
}
