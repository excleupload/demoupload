package com.example.tapp.common.dto;

import java.io.Serializable;

import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.data.entities.StaticPages;

public class StaticPagesDto implements Serializable {

	public static final String ID = "id";
	public static final String CONTEXT = "content";
	public static final String TYPE = "type";

	private static final long serialVersionUID = 1L;

	private Long id;
	private String content;
	private PageType type;
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

	public PageType getType() {
		return type;
	}

	public void setType(PageType type) {
		this.type = type;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	private Long createdOn;

	public StaticPagesDto() {
		// Default
	}

	public StaticPagesDto(StaticPages pages) {
		this.id = pages.getId();
		this.content = pages.getContent();
		this.type = pages.getType();
		this.createdOn = pages.getCreatedOn().getTime();
	}

   
   

}
