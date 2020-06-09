package com.tcts.foresight.pojo;

public class Login {

	private String username;
	private String password;
	private String token;
	private String userRole;
	private String userTheme;

	public String getUserTheme() {
		return userTheme;
	}
	public void setUserTheme(String userTheme) {
		this.userTheme = userTheme;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
