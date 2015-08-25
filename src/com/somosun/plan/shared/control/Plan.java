package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author Mike W (some old stuff is from cesar)
 */
@Entity
public class Plan implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Index @Id Long id=null;
	private String name = null;
	@Index private Career career = null;
    private List<Semester> semesters = null;
    private double gpa = 0;
    @Index private Student user = null;
    @Index private boolean isDefault = false;

    public Plan() {
    }
    
    public Career getCareer(){
    	return career;
    }
    
    public String getCareerCode(){
    	return career.getCode();
    }

	public Long getId() {
		return id;
	}

	public List<Semester> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<Semester> semesters) {
		this.semesters = semesters;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double papa) {
		this.gpa = papa;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getUser() {
		return user;
	}

	public void setUser(Student user) {
		this.user = user;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void calculateGpa() {
		int credits = 0;
		double sum = 0;
		for(Semester semester : semesters){
			for(SubjectValue subjectValue : semester.getSubjects()){
				if(subjectValue.isTaken()){
					if(subjectValue.getComplementaryValues().getSubject().isApprovenType() == false){						
						sum += (subjectValue.getGrade()*(subjectValue.getComplementaryValues().getSubject().getCredits()));
						credits += subjectValue.getComplementaryValues().getSubject().getCredits();
					}
				}
			}
		}
		this.setGpa((double) sum/credits);
	}

}
