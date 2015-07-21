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
    private boolean isApprovenType = false;
    /**
     * A special subject is a kind of subject that will NOT be shown as a subject in the planPresenter, but still need to be a subject. e.g. "80% de los créditos de..."
     */
    @Index private boolean special  = false;
    /**
     * A dummy subject is a kind of subject that will be shown as a subject in the planPresenter, but still need to be a subject, and can appears in 
     * searches only if searching Old/Uncontinued subjects. e.g. Old subjects
     */
    @Index private boolean isDummy = false;
    /**
     * A default subject is a kind of subject that will be shown as a subject in the planPresenter, but still need to be a subject, and will never ever
     * appear in a search (not even as a sia backup). e.g. "optativa [SubjectGroup.getName()]" 
     */
    @Index private boolean isDefault = false;
    
    /**
     * This will not allow the search by location, jut by Id, Name and Code.
     * 
     * The use of this constructor is discourage  
     */
    public Subject(){
    }
    
    public Subject(int credits, String code, String siaCode, String name, String location) {
    	this.setCredits(credits);
    	this.setCode(code);
    	this.setSiaCode(siaCode);
    	this.setName(name);
    	this.setLocation(location);
    }
    
//    public Subject(int credits, String code, String siaCode, String name, String location) {
//    	this.setCredits(credits);
//    	this.setCode(code);
//    	this.setSiaCode(siaCode);
//    	this.setName(name);
//    	this.setLocation(location);
//    	this.setSpecial(special);
//    }
//    
//    public Subject(int credits, String code, String siaCode, String name, String location) {
//    	this.setCredits(credits);
//    	this.setCode(code);
//    	this.setSiaCode(siaCode);
//    	this.setName(name);
//    	this.setLocation(location);
//    	this.setSpecial(special);
//    	this.setDummy(isDummy);
//    }
    
    /**
     * true if everything but he id is equal 
     * @param subject
     * @return
     */
    public boolean equals(Subject subject){
    	boolean b = true;
    	if(subject != null){
    		if(this.credits != subject.getCredits())
    			b = false;
    		
    		if(this.getCode() == null)
    		{
    			if(subject.getCode() != null) b = false;
    		}
    		else
    		{
    			if(this.getCode().equals(subject.getCode()) == false) b = false;
    		}
    		
    		if(this.getName() == null)
    		{
    			if(subject.getName() != null) b = false;
    		}
    		else
    		{
    			if(this.getName().equals(subject.getName()) == false) b = false;
    		}
    		
    		if(this.getSiaCode() == null)
    		{
    			if(subject.getSiaCode() != null) b = false;
    		}
    		else
    		{
    			if(this.getSiaCode().equals(subject.getSiaCode()) == false) b = false;
    		}
    		
    		if(this.getLocation() == null)
    		{
    			if(subject.getLocation() != null) b = false;
    		}
    		else
    		{
    			if(this.getLocation().equals(subject.getLocation()) == false) b = false;
    		}		
    	}
    	return b;
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

	public void setId(Long id) {
		this.id = id;
	}

	/**
     * A special subject is a kind of subject that will never be show as a subject in the planPresenter, but still need to be a subject. e.g. "80% de los créditos de..."
     */
	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
		this.isDefault = false;
		this.isDummy = false;
	}

    /**
     * A dummy subject is a kind of subject that will be shown as a subject in the planPresenter, but still need to be a subject. e.g. "optativa [SubjectGroup.getName()]"
     */
	public boolean isDummy() {
		return isDummy;
	}

	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
		this.isDefault = false;
		this.special = false;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
		this.special = false;
		this.isDummy = false;
	}

	public boolean isApprovenType() {
		return isApprovenType;
	}

	public void setApprovenType(boolean isApprovenType) {
		this.isApprovenType = isApprovenType;
	}
}
   