package com.example.tapp.common.dto;
import java.io.Serializable;

public class ShareContentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ID = "Id";
    public static final String TEXT_CONTENT = "textContent";
    public static final String APP_LINK = "appLink";
    public static final String CREATED_ON = "createdOn";
    public static final String MODIFIED_ON = "modifiedOn";

    private Integer Id;
    private String textContent;
    private String appLink;
    private Long createdOn;
    private Long modifiedOn;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
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
	public Long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	public Long getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

 
   

}