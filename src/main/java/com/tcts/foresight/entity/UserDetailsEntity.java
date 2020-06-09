package com.tcts.foresight.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcts.foresight.pojo.UserDetailsDTO;

@Entity
@Table(name = "user_details")
public class UserDetailsEntity extends AuthMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userdetails_id_seq_generator")
	@SequenceGenerator(name = "userdetails_id_seq_generator", sequenceName = "userdetails_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Transient
    private String newpass;
	
	@Transient
    private boolean isPasswordResetRequiredByAdmin;
	
	@Column(name = "user_id")
	private String userId;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@Column(name = "fullname")
	private String fullName;

	@Transient
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "contactno")
	private String contactNo;

	@Column(name = "islogin")
	private String isLogin;

	@Column(name = "user_theme")
	private String userTheme;

	@Column(name = "profile_photo")
	private String userPhoto;

	@Column(name = "authtoken")
	private String authToken;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "otp_count")
	private Long otp_count ;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_groups", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id", unique = false) })
	private List<GroupDetailsEntity> groupList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}

	public String getUserTheme() {
		return userTheme;
	}

	public void setUserTheme(String userTheme) {
		this.userTheme = userTheme;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public List<GroupDetailsEntity> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupDetailsEntity> groupList) {
		this.groupList = groupList;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getNewpass() {
		return newpass;
	}

	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	
	public boolean getIsPasswordResetRequiredByAdmin() {
		return isPasswordResetRequiredByAdmin;
	}

	public void setPasswordResetRequiredByAdmin(boolean isPasswordResetRequiredByAdmin) {
		this.isPasswordResetRequiredByAdmin = isPasswordResetRequiredByAdmin;
	}
	

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Long getOtp_count() {
		return otp_count;
	}

	public void setOtp_count(Long otp_count) {
		this.otp_count = otp_count;
	}

	@JsonIgnore
	public UserDetailsDTO getUserDetailsDTO() {
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
		userDetailsDTO.setId(this.getId());
		userDetailsDTO.setUserId(this.getUserId());
		userDetailsDTO.setEmail(this.getEmail());
		userDetailsDTO.setUserTheme(this.getUserTheme());
		userDetailsDTO.setFullName(this.getFullName());

		return userDetailsDTO;
	}

	
	//ToString
	@Override
	public String toString() {
		return "UserDetailsEntity [id=" + id + ", newpass=" + newpass + ", isPasswordResetRequiredByAdmin="
				+ isPasswordResetRequiredByAdmin + ", userId=" + userId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", fullName=" + fullName + ", password=" + password + ", email=" + email + ", contactNo="
				+ contactNo + ", isLogin=" + isLogin + ", userTheme=" + userTheme + ", userPhoto=" + userPhoto
				+ ", authToken=" + authToken + ", otp=" + otp + ", otp_count=" + otp_count + ", groupList=" + groupList
				+ "]";
	}

	
	
	
	
}
