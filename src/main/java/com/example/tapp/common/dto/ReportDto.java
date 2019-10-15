package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import com.example.tapp.common.discriminator.ReportStatus;
import com.example.tapp.data.entities.Report;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ReportDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String REPORTER_ID = "reporterId";
    public static final String REPORTED_ID = "reportedId";
    public static final String REASON = "reason";

    private UUID id;
    private UUID reporterId;
    private UUID reportedId;
    private String reason;
    private HashMap<String, String> files;

    private UserDto reporterUser;
    private UserDto reportedUser;
    private Long createdOn;
    private ReportStatus status;
    private String proofImage;
    private Report proofImg;
    private String replyAdmin;

    private Boolean isRead = false;


    public ReportDto() {
    }
    public ReportDto(Report report) {
        this.id = report.getId();
        this.reason = report.getReason();
    }


	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public UUID getReporterId() {
		return reporterId;
	}


	public void setReporterId(UUID reporterId) {
		this.reporterId = reporterId;
	}


	public UUID getReportedId() {
		return reportedId;
	}


	public void setReportedId(UUID reportedId) {
		this.reportedId = reportedId;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public HashMap<String, String> getFiles() {
		return files;
	}


	public void setFiles(HashMap<String, String> files) {
		this.files = files;
	}


	public UserDto getReporterUser() {
		return reporterUser;
	}


	public void setReporterUser(UserDto reporterUser) {
		this.reporterUser = reporterUser;
	}


	public UserDto getReportedUser() {
		return reportedUser;
	}


	public void setReportedUser(UserDto reportedUser) {
		this.reportedUser = reportedUser;
	}


	public Long getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}


	public ReportStatus getStatus() {
		return status;
	}


	public void setStatus(ReportStatus status) {
		this.status = status;
	}


	public String getProofImage() {
		return proofImage;
	}


	public void setProofImage(String proofImage) {
		this.proofImage = proofImage;
	}


	public Report getProofImg() {
		return proofImg;
	}


	public void setProofImg(Report proofImg) {
		this.proofImg = proofImg;
	}


	public String getReplyAdmin() {
		return replyAdmin;
	}


	public void setReplyAdmin(String replyAdmin) {
		this.replyAdmin = replyAdmin;
	}


	public Boolean getIsRead() {
		return isRead;
	}


	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
    
}
  

 
