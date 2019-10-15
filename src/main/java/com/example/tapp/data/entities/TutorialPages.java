package com.example.tapp.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.example.tapp.common.dto.TutorialPagesDto;

@Entity
@Table(name = "tutorial_pages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TutorialPages extends BaseEntity<Long> {

    @Column(name = "id", length = 16)
    private Long Id;


    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean imagecheck = false;

    @Column(nullable = false)
    private Boolean videocheck = false;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
