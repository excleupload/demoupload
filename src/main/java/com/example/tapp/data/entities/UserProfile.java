package com.example.tapp.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.Gender;

@Entity
@Table(name = "user_profile")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserProfile extends BaseEntityUUID {

    private String profileImageName;

    private int age;

    private long dob;

    private String jobTitle;

    private String educationPlace;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(length = 500)
    private String address;

    private String webSite;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "profile")
    private List<UserSocialMedia> socialMedia = new ArrayList<>();

	public String getProfileImageName() {
		return profileImageName;
	}

	public void setProfileImageName(String profileImageName) {
		this.profileImageName = profileImageName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getDob() {
		return dob;
	}

	public void setDob(long dob) {
		this.dob = dob;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEducationPlace() {
		return educationPlace;
	}

	public void setEducationPlace(String educationPlace) {
		this.educationPlace = educationPlace;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<UserSocialMedia> getSocialMedia() {
		return socialMedia;
	}

	public void setSocialMedia(List<UserSocialMedia> socialMedia) {
		this.socialMedia = socialMedia;
	}



}