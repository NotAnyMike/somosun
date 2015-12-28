package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;

public abstract class SubjectValueAbstract implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Ignore private String subjectValuesPublicId = null;
    private Double grade = null;
    private boolean taken = false;

	abstract public Group getGroup();
	abstract public void setGroup(Group group);
	abstract public ComplementaryValue getComplementaryValue();
	abstract public void setComplementaryValue(ComplementaryValue complementaryValue);

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

	public void setId(Long id) {
		this.id = id;
	}
	
}
