package com.example.tapp.data.entities;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.dto.ConnectionRequestDto;

@Entity
@Table(name = "connection_request")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ConnectionRequest extends BaseEntityUUID {

    public static final String FIRST_USER = "firstUser";
    public static final String SECOND_USER = "secondUser";
    public static final String STATUS = "status";
    public static final String FIRST_USER_STATUS = "firstUserStatus";
    public static final String SECOND_USER_STATUS = "secondUserStatus";
    public static final String PLACE = "place";

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private User firstUser;

    public User getFirstUser() {
		return firstUser;
	}



	public void setFirstUser(User firstUser) {
		this.firstUser = firstUser;
	}



	public User getSecondUser() {
		return secondUser;
	}



	public void setSecondUser(User secondUser) {
		this.secondUser = secondUser;
	}



	public BigDecimal getLatitude() {
		return latitude;
	}



	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}



	public BigDecimal getLongitude() {
		return longitude;
	}



	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}



	public ConnectionRequestStatus getStatus() {
		return status;
	}



	public void setStatus(ConnectionRequestStatus status) {
		this.status = status;
	}



	public boolean isFirstUserAccepted() {
		return firstUserAccepted;
	}



	public void setFirstUserAccepted(boolean firstUserAccepted) {
		this.firstUserAccepted = firstUserAccepted;
	}



	public boolean isSecondUserAccepted() {
		return secondUserAccepted;
	}



	public void setSecondUserAccepted(boolean secondUserAccepted) {
		this.secondUserAccepted = secondUserAccepted;
	}



	public ConnectionRequestStatus getFirstUserStatus() {
		return firstUserStatus;
	}



	public void setFirstUserStatus(ConnectionRequestStatus firstUserStatus) {
		this.firstUserStatus = firstUserStatus;
	}



	public ConnectionRequestStatus getSecondUserStatus() {
		return secondUserStatus;
	}



	public void setSecondUserStatus(ConnectionRequestStatus secondUserStatus) {
		this.secondUserStatus = secondUserStatus;
	}



	public String getPlace() {
		return place;
	}



	public void setPlace(String place) {
		this.place = place;
	}



	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private User secondUser;

    @Column(columnDefinition = "DECIMAL(19,6)")
    private BigDecimal latitude;

    @Column(columnDefinition = "DECIMAL(19,6)")
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConnectionRequestStatus status;

    private boolean firstUserAccepted = false;

    private boolean secondUserAccepted = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConnectionRequestStatus firstUserStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConnectionRequestStatus secondUserStatus;

    private String place;

   

    public ConnectionRequestDto dto() {
        ConnectionRequestDto dto = new ConnectionRequestDto();
        dto.setId(this.getId());
        dto.setStatus(this.status);
        dto.setTime(this.getCreatedOn().getTime());
        dto.setPlace(this.place);
        dto.setLatitude(this.latitude);
        dto.setLongitude(this.longitude);
        dto.setFirstUserAccepted(this.firstUserAccepted);
        dto.setSeconUserAccepted(this.secondUserAccepted);

        this.firstUser.setAuthToken(null);
        dto.setFirstUser(this.firstUser.toDto());

        this.secondUser.setAuthToken(null);
        dto.setSecondUser(this.secondUser.toDto());
        return dto;
    }
}
