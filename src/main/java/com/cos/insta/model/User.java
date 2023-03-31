package com.cos.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@Entity(name = "user") // JPA -> ORM
public class User {

	@ Id		// Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전량을 따라간다.
	private int id;						// 시퀀스
	
	@Column(nullable = false, length = 30, unique = true)
	private String username;	// 사용자 아이디
	
	@Column(nullable = false, length = 100)
	private String password;	// 암호화된 패스워드(해쉬)
	
	@Column(nullable = false, length = 50)
	private String name;			// 사용자 이름
	
	@Column(length = 200)
	private String website;		// 홈페이지 주소
	
	@Column(length = 100)
	private String bio;				// 자기 소개
	
	@Column(length = 50)
	private String email;
	
	@Column(length = 20)
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 5)
	private GenderType gender;
	
	private String profileImage;	// 프로파일 사진 경로 + 이름
	
	// (1) findById() 때만 동작
	// (2) findByUserInfo() 제외
	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties({"user", "tags", "likes"})
	@Builder.Default // @Builder 는 초기화 표현을 완전히 무시하므로 초기화 해준다.
	private List<Image> images = new ArrayList<>();	
	
	@CreationTimestamp			// 자동으로 현재시간 세팅
	private Timestamp createDate;
	
	@CreationTimestamp
	private Timestamp updateDate;
	
}
