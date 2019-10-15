package com.example.tapp.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.common.dto.StaticPagesDto;

@Entity
@Table(name = "static_pages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class StaticPages extends BaseEntity<Long> {
	
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PageType type;

    @Column(columnDefinition = "TEXT")
    private String content;

   
    public PageType getType() {
		return type;
	}


	public void setType(PageType type) {
		this.type = type;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public StaticPagesDto toDto() {
        return new StaticPagesDto(this);
    }

}