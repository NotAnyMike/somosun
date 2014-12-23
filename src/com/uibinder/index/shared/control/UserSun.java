package com.uibinder.index.shared.control;

import java.io.Serializable;

/**
 *
 * @author Mike.
 */
public class UserSun implements Serializable {
	
	public UserSun(){
	}

    public UserSun(String name, String username, String email, String id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    private String name;
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
    
    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    private String username;
    private String email;
    private String id;
    
}
