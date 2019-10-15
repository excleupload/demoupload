package com.example.tapp.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.example.tapp.common.dto.ShareContentDto;

@Entity
@Table(name = "share_content")
public class ShareContent extends BaseEntity<Integer> {

    @Column(name = "text_content", columnDefinition = "TEXT")
    private String textContent;

    @Column(name = "app_link", columnDefinition = "TEXT")
    private String appLink;



    public String getTextContent() {
		return textContent;
	}



	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}



	public String getAppLink() {
		return appLink;
	}



	public void setAppLink(String appLink) {
		this.appLink = appLink;
	}



	public ShareContentDto toDto() {
        ShareContentDto dto = new ShareContentDto();
        dto.setId(this.getId());
        dto.setTextContent(this.textContent);
        dto.setAppLink(this.appLink);
        dto.setCreatedOn(this.getCreatedOn().getTime());
        dto.setModifiedOn(this.getModifiedOn().getTime());
        return dto;
    }

}
