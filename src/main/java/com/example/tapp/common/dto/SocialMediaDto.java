package com.example.tapp.common.dto;

import com.example.tapp.common.discriminator.SocialMedia;

public class SocialMediaDto {
    public static final String SOCIAL_MEDIA = "socialMedia";
    public static final String CONTENT = "content";
    public static final String SHARED = "shared";

    private SocialMedia socialMedia;
    private String content;
    public SocialMedia getSocialMedia() {
		return socialMedia;
	}
	public void setSocialMedia(SocialMedia socialMedia) {
		this.socialMedia = socialMedia;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	private boolean shared;

  
}
