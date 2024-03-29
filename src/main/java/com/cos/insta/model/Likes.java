package com.cos.insta.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "likes")
@Entity(name = "likes")
public class Likes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "userId")	 // Foreign Key로 사용되어질 이름
	@JsonIgnoreProperties({ "images", "password", "name", "website", "bio", "email", "phone", "gender", "createDate", 	"updateDate" }) // JSON 직렬, 역직렬화를 무시
	private User user; // id, username, profileImage
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "imageId")
	@JsonIgnoreProperties({ "tags", "user", "likes" })
	private Image image; // 기본 : image_id

	@CreationTimestamp
	private Timestamp createDate;

	@CreationTimestamp
	private Timestamp updateDate;

}
