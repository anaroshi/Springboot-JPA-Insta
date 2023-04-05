package com.cos.insta.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoProfile {
	
	private String id;
	private String connected_at;
	private Properties properties;
	private KakaoAccount kakao_account;
	
	@Data
	public class Properties {
		private String nickname;
		private String profile_image;
		private String thumbnail_image;
	}
	
	@Data
	public class KakaoAccount {
		private boolean profile_needs_agreement;
		private boolean profile_nickname_needs_agreement;
		private Profile profile;
		private boolean has_email;
		private boolean email_needs_agreement;
		public Boolean is_email_valid;
		public Boolean is_email_verified;
		private String email;
		
		@Data
		public class Profile {
			private String nickname;
			private String thumbnail_image_url;
			private String profile_image_url;			
		}
	}
}