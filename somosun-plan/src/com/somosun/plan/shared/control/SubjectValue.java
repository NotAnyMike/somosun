package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author Mike, of stuff is from Cesar A. Villamizar
 */
@Cache(expirationSeconds=9000)
@Entity
public class SubjectValue implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private Group group = null;
	@Ignore private String subjectValuesPublicId = null;
    private Double grade = null;
    private boolean taken = false;
    private ComplementaryValue complementaryValue = null;
    
    /**
     * people should try to avoid using this constructor
     */
    public SubjectValue(){
    	this.complementaryValue = new ComplementaryValue();
    }

    public SubjectValue(Group group, double grade,boolean taken, ComplementaryValue complementaryValue) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.complementaryValue = complementaryValue;
    }
    
    public SubjectValue(double grade,boolean taken, ComplementaryValue complementaryValue) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.complementaryValue = complementaryValue;
    }
    
    public SubjectValue(Group group, double grade,boolean taken) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.complementaryValue = new ComplementaryValue();
    }

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		if(grade == null || (grade >= 0 && grade <= 5)){			
			this.grade = grade;
			if(grade == null) setTaken(false);
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
		if(taken == false){
			setGrade(0.0);
		}
	}

	public String getSubjectValuePublicId() {
		return subjectValuesPublicId;
	}

	public void setSubjectValuesPublicId(String subjectValuesPublicId) {
		this.subjectValuesPublicId = subjectValuesPublicId;
	}

	public ComplementaryValue getComplementaryValue() {
		return complementaryValue;
	}

	public void setComplementaryValue(ComplementaryValue complementaryValue) {
		this.complementaryValue = complementaryValue;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
