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
					if(subjectValue != null && subjectValue.getComplementaryValue() != null && subjectValue.getComplementaryValue().getSubject() != null){						
						if(subjectValue.getComplementaryValue().getSubject().isApprovenType() == false){						
							sum += (subjectValue.getGrade()*(subjectValue.getComplementaryValue().getSubject().getCredits()));
							credits += subjectValue.getComplementaryValue().getSubject().getCredits();
						}
					}
				}
			}
		}
		this.setGpa((double) sum/credits);
	}
	
	public boolean compare(PlanAbstract p){
		boolean toReturn = false;
		
		if(p != null && this.getName().equals(p.getName()) && (this.getUser().getIdSun().equals(p.getUser().getIdSun()))  && this.getGpa() == p.getGpa() &&
				this.getId().equals(p.getId()) && this.getCareer().equals(p.getCareer())){
			//Check the semesters
			//if null or empty both
			if((this.getSemesters() == null && p.getSemesters() == null) || (this.getSemesters() != null && this.getSemesters().isEmpty() && p.getSemesters() != null & p.getSemesters().isEmpty())){
				toReturn = true;
			}else{
				if(this.getSemesters() != null && p.getSemesters() != null && this.getSemesters().size() == p.getSemesters().size()){
					boolean isEqual = true;
					for(int x = 0; x < this.getSemesters().size(); x++){
						if(this.getSemesters().get(x).equals(p.getSemesters().get(x)) == false){
							isEqual = false;
							break;
						}
					}
					if(isEqual == true) toReturn = true;
				}
			}
		}
		
		return toReturn;
	}

}
