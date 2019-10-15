package com.example.tapp.data.entities;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.example.tapp.common.dto.AdminUserDto;

@Entity
@Table(name = "admin_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Where(clause = "deleted = false")
public class AdminUser extends BaseEntity<Integer> {

    private String name;
    private String email;
    private String password;
    private String authToken;
    private Long lastLogin;
    private String fpToken; // for got password token
    public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getAuthToken() {
		return authToken;
	}



	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}



	public Long getLastLogin() {
		return lastLogin;
	}



	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}



	public String getFpToken() {
		return fpToken;
	}



	public void setFpToken(String fpToken) {
		this.fpToken = fpToken;
	}



	public long getFpTokenCreatedOn() {
		return fpTokenCreatedOn;
	}



	public void setFpTokenCreatedOn(long fpTokenCreatedOn) {
		this.fpTokenCreatedOn = fpTokenCreatedOn;
	}



	public boolean isFpTokenUsed() {
		return fpTokenUsed;
	}



	public void setFpTokenUsed(boolean fpTokenUsed) {
		this.fpTokenUsed = fpTokenUsed;
	}



	private long fpTokenCreatedOn;
    private boolean fpTokenUsed = false;

   

    public AdminUserDto dto() {
        return Stream.of(this).map(AdminUserDto::new).findFirst().orElse(new AdminUserDto());
    }
}
