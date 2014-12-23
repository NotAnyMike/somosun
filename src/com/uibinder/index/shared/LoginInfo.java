package com.uibinder.index.shared;

import java.io.Serializable;

import com.uibinder.index.shared.control.UserSun;

public class LoginInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	public String getLogoutUrl() {
		return logoutUrl;
	}

	private UserSun user;

	public UserSun getUser() {
		return user;
	}

	public void setUser(UserSun user) {
		this.user = user;
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
