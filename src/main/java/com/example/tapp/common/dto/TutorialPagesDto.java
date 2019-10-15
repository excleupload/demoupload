package com.example.tapp.common.dto;

import java.io.Serializable;

import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.entities.TutorialPages;

public class TutorialPagesDto implements Serializable {

    public static final String CONTEXT = "content";
    public static final String ID = "id";
    private static final long serialVersionUID = 1L;

    private Long id;
    private String content;
    private Long createdOn;
    private Boolean imagecheck = false;
    private Boolean videocheck = false;


    public TutorialPagesDto() {
    }
    public TutorialPagesDto(TutorialPages pages) {
        this.id = pages.getId();
        this.content = pages.getContent();
        this.createdOn = pages.getCreatedOn().getTime();
        this.imagecheck = pages.getImagecheck();
        this.videocheck = pages.getVideocheck();
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	public Boolean getImagecheck() {
		return imagecheck;
	}
	public void setImagecheck(Boolean imagecheck) {
		this.imagecheck = imagecheck;
	}
	public Boolean getVideocheck() {
		return videocheck;
	}
	public void setVideocheck(Boolean videocheck) {
		this.videocheck = videocheck;
	}



}
