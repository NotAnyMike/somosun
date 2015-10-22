package com.somosun.plan.shared;

import java.io.Serializable;

import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.UserSun;

public class LoginInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private Student student;
	
	public LoginInfo(){
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public boolean isLoggedIn(){
		return loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

}
