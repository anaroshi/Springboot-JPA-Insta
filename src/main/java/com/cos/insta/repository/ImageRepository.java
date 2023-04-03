package com.cos.insta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.insta.model.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	@Query(
			value="SELECT * FROM image WHERE userId in (SELECT toUserId FROM follow WHERE fromUserId=?1)", nativeQuery=true
	)
	Page<Image> findImage(int userId, Pageable pageable);

}
