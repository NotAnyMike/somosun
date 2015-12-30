package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.controlAbstract.SubjectGroupAbstract;

public class SubjectGroup extends SubjectGroupAbstract<SubjectGroup> implements Serializable {

	private Career career = null;

	public SubjectGroup(){}
	
	public SubjectGroup(String name, Career career, Boolean fundamental, int obligatoryCredits, int optativeCredits, boolean error) {
		setName(name);
		setCareer(career);
		setFundamental(fundamental);
		setObligatoryCredits(obligatoryCredits);
		setOptativeCredits(optativeCredits);
		setError(error);
	}
	
	public boolean equals(SubjectGroup g){
		if(this.getName() == g.getName() && this.getCareer().equals(g.getCareer()) && this.isFundamental() == g.isFundamental() && this.getObligatoryCredits() == g.getObligatoryCredits() && this.getOptativeCredits() == g.getOptativeCredits()){
			return true;
		} else {
			return false;
		}
	}
	
	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

}
