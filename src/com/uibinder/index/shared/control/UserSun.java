package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author Mike.
 * The difference between id and idSun is that idSun is a Long genereted by us, while id is the google unique id, 
 * and because we are allowing users as guest then we will have some profiles with no id but with some idSun
 */
@Entity
public class UserSun implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long idSun = null; 
	@Index private String idG = null;
	@Index private String username = null;
	@Index private String email = null;
	private String name = null;
	private boolean blocked = false;
	private boolean admin = false;
	
	public UserSun(){
	}

	public UserSun(String id, String name, String username, String email) {
		this.idG = id;
		this.username = username;
		this.name = name;
		this.email = email;
	}
	
	public Long getIdSun() {
		return idSun;
	}
	
	public void setIdSun(Long idSun) {
		this.idSun = idSun;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getIdG() {
    	return idG;
    }
    
    public void setIdG(String id) {
    	this.idG = id;
    }
    
    public boolean isUnal(){
    	return false;
    }
    
}
