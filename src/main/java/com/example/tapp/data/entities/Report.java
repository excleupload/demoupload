package com.example.tapp.data.entities;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.ReportStatus;
import com.example.tapp.common.dto.ReportDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.utils.AppUtils;

@Entity
@Table(name = "report")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Report extends BaseEntityUUID {

    public static final String REPORTER_USER = "reporterUser";
    public static final String STATUS = "status";
    public static final String IS_READ = "isRead";
    public static final String REPORTED_USER = "reportedUser";

    @ManyToOne
    @JoinColumn(name = "reporter_user")
    private User reporterUser;

    @ManyToOne
    @JoinColumn(name = "reported_user")
    private User reportedUser;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String proofImage;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)

    private ReportStatus status;

    @Column(nullable = false)
    private Boolean isRead = false;

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    @Column(columnDefinition = "TEXT")
    private String replyAdmin;

  
    public User getReporterUser() {
		return reporterUser;
	}

	public void setReporterUser(User reporterUser) {
		this.reporterUser = reporterUser;
	}

	public User getReportedUser() {
		return reportedUser;
	}

	public void setReportedUser(User reportedUser) {
		this.reportedUser = reportedUser;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getProofImage() {
		return proofImage;
	}

	public void setProofImage(String proofImage) {
		this.proofImage = proofImage;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

	public String getReplyAdmin() {
		return replyAdmin;
	}

	public void setReplyAdmin(String replyAdmin) {
		this.replyAdmin = replyAdmin;
	}

	@SuppressWarnings("unchecked")
    public ReportDto dto() {
        ReportDto dto = new ReportDto();
        dto.setId(this.getId());
        dto.setId(this.getId());
        dto.setReason(this.reason);
        dto.setCreatedOn(this.getCreatedOn().getTime());
        dto.setStatus(this.status);
        dto.setReplyAdmin(this.replyAdmin);


        try {
            dto.setFiles(this.proofImage == null ? null : AppUtils.jsonParse(this.proofImage, HashMap.class));
            dto.setProofImage(this.proofImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        UserDto reporterUser = new UserDto();
        reporterUser.setId(this.reporterUser.getId());
        reporterUser.setFirstName(this.reporterUser.getFirstName());
        reporterUser.setLastName(this.reporterUser.getLastName());
        reporterUser.setProfileImage(this.reporterUser.getProfile().getProfileImageName());
        dto.setReporterUser(reporterUser);

        UserDto reportedUser = new UserDto();
        reportedUser.setId(this.reportedUser.getId());
        reportedUser.setFirstName(this.reportedUser.getFirstName());
        reportedUser.setLastName(this.reportedUser.getLastName());
        reportedUser.setProfileImage(this.reportedUser.getProfile().getProfileImageName());
        dto.setReportedUser(reportedUser);
        return dto;
    }
}