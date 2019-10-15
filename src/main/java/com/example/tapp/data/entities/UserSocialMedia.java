package com.example.tapp.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.SocialMedia;
import com.example.tapp.common.dto.SocialMediaDto;

@Entity
@Table(name = "user_social_media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserSocialMedia extends BaseEntity<Long> {

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private SocialMedia media;

    private boolean shared;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

 
    public SocialMediaDto dto() {
        SocialMediaDto dto = new SocialMediaDto();
        dto.setContent(this.content);
        dto.setSocialMedia(this.media);
        dto.setShared(this.shared);
        return dto;
    }


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public SocialMedia getMedia() {
		return media;
	}


	public void setMedia(SocialMedia media) {
		this.media = media;
	}


	public boolean isShared() {
		return shared;
	}


	public void setShared(boolean shared) {
		this.shared = shared;
	}


	public UserProfile getProfile() {
		return profile;
	}


	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
}
