package com.cos.insta.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "follow")
@Entity(name = "follow")
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// 중간 테이블 생성됨. 
	// fromUser가 toUser를 following 함.
	// toUserr를 fromUse가 following 함.

	@ManyToOne
	@JoinColumn(name = "fromUserId")
	@JsonIgnoreProperties({"images"})
	private User fromUser;
	
	@ManyToOne
	@JoinColumn(name = "toUserId")
	@JsonIgnoreProperties({"images"})
	private User toUser; 
	
	@Transient
	private boolean followState;
	
	@CreationTimestamp
	private Timestamp createDate;

	@CreationTimestamp
	private Timestamp updateDate;
	
}
