package com.cos.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "image")
@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String location;	// 사진 찍은 위치
	
	private String caption;	// 사진 설명
	
	private String postImage;	// 포스팅 사진 경로 + 이름
	
	@ManyToOne
	@JoinColumn(name="userId")	 // Foreign Key로 사용되어질 이름
	@JsonIgnoreProperties({"password", "images"})
	private User user;// DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다.
	
	// Like List	
	@OneToMany(mappedBy = "image")
	private List<Likes> likes = new ArrayList<>();
	
	// Tag List
	@OneToMany(mappedBy = "image")
	@JsonManagedReference
	private List<Tag> tags = new ArrayList<>();
	
	@Transient 	// DB에 영향을 미치지 않는다.
	private int likeCount;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	@CreationTimestamp
	private Timestamp updateDate;
}
