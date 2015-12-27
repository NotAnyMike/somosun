package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.server.control.SemesterServer;
import com.somosun.plan.shared.control.controlAbstract.PlanAbstract;
import com.somosun.plan.shared.control.controlAbstract.SemesterAbstract;

/**
 *
 * @author Mike W
 */
public class Plan extends PlanAbstract implements Serializable {

	private Career career = null;
	private Student user = null;
    private List<Semester> semesters = null;
	
    public Plan() {
    }
    
    public Career getCareer(){
    	return career;
    }
    
    public String getCareerCode(){
    	return career.getCode();
    }
    
    public List<Semester> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<Semester> semesters) {
		this.semesters = semesters;
	}
	
	public void setCareer(Career career) {
		this.career = career;
	}
	
	public Student getUser() {
		return user;
	}

	public void setUser(Student user) {
		if(user == null){
			this.user = null;
		}else{			
			this.user = user;
		}
	}
	
	public void calculateGpa() {
		int credits = 0;
		double sum = 0;
		for(Semester semester : semesters){
			for(SubjectValue subjectValue : semester.getSubjects()){
				if(subjectValue.isTaken()){
					if(subjectValue.getComplementaryValue().getSubject().isApprovenType() == false){						
						sum += (subjectValue.getGrade()*(subjectValue.getComplementaryValue().getSubject().getCredits()));
						credits += subjectValue.getComplementaryValue().getSubject().getCredits();
					}
				}
			}
		}
		this.setGpa((double) sum/credits);
	}

}
