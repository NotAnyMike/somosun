package com.uibinder.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Agrupaciones
 * 
 * @author Mike
 *
 */
@Entity
public class SubjectGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private String name = null;
	@Index private Career career = null;
	@Index private Boolean fundamental = null;
	private int obligatoryCredits = 0;
	private int optativeCredits = 0;
	@Index private boolean error = false;
	
	public SubjectGroup(){
	}
	
	public SubjectGroup(String name, Career career, Boolean fundamental, int obligatoryCredits, int optativeCredits, boolean error) {
		super();
		this.name = name;
		this.career = career;
		this.fundamental = fundamental;
		this.obligatoryCredits = obligatoryCredits;
		this.optativeCredits = optativeCredits;
		this.error = error;
	}
	
	public boolean equals(SubjectGroup g){
		if(this.name == g.getName() && this.career.equals(g.getCareer()) && this.fundamental == g.isFundamental() && this.obligatoryCredits == g.getObligatoryCredits() && this.optativeCredits == g.getOptativeCredits()){
			return true;
		} else {
			return false;
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public Boolean isFundamental() {
		return fundamental;
	}

	public void setFundamental(Boolean fundamental) {
		this.fundamental = fundamental;
	}

	public int getObligatoryCredits() {
		return obligatoryCredits;
	}

	public void setObligatoryCredits(int obligatoryCredits) {
		this.obligatoryCredits = obligatoryCredits;
	}

	public int getOptativeCredits() {
		return optativeCredits;
	}

	public void setOptativeCredits(int optativeCredits) {
		this.optativeCredits = optativeCredits;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean ok) {
		this.error = ok;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
