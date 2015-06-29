package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author Mike
 * 
 * The Serializable part to allow server-client communication, hope you don't mind 
 * and moved it to the shared package cuz the client-side will use it
 *  
 */
@Entity
public class Subject implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
    private int credits = 0;
    @Index private String name = null;
    @Index private String code = null;   
    @Index private String siaCode = null;
    private String location = null;
    @Index private boolean special  = false; //This is for example in the case the prerequisite is 80% of the ... 
    //in the case the subject is special I can create a dummy subjectValue in order to create show it in the planPresenter view
    
    /**
     * This will not allow the search by location, jut by Id, Name and Code.
     * 
     * The use of this constructor is discourage  
     */
    public Subject(){
    }
    
    public Subject(int credits, String code, String siaCode, String name, String location) {
    	this.credits = credits;
    	this.code = code;
    	this.siaCode = siaCode;
    	this.name = name;
    	this.location = location;
    }
    
    public Subject(int credits, String code, String siaCode, String name, String location, boolean special) {
    	this.credits = credits;
    	this.code = code;
    	this.siaCode = siaCode;
    	this.name = name;
    	this.location = location;
    	this.special = special;
    }
    
    /**
     * true if everything but he id is equal 
     * @param subject
     * @return
     */
    public boolean equals(Subject subject){
    	if(this.credits == subject.getCredits() && this.name.equals(subject.getName()) && this.code.equals(subject.getCode()) && this.siaCode.equals(subject.getSiaCode()) && this.location.equals(subject.getLocation())){
    		return true;
    	} else {
    		return false;
    	}
    }

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public String getSiaCode() {
		return siaCode;
	}

	public void setSiaCode(String siaCode) {
		this.siaCode = siaCode;
	}
}
   