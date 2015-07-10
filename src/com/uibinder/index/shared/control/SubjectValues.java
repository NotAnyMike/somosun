package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author Mike, of stuff is from Cesar A. Villamizar
 */
@Entity
public class SubjectValues implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private Group group = null;
	@Ignore private String subjectValuesPublicId = null;
    private double grade = 0;
    private boolean taken = false;
    private ComplementaryValues complementaryValues = null;
    
    /**
     * people should try to avoid using this constructor
     */
    public SubjectValues(){
    	this.complementaryValues = new ComplementaryValues();
    }

    public SubjectValues(Group group, double grade,boolean taken, ComplementaryValues complementaryValues) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.complementaryValues = complementaryValues;
    }
    
    public SubjectValues(double grade,boolean taken, ComplementaryValues complementaryValues) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.complementaryValues = complementaryValues;
    }
    
    public SubjectValues(Group group, double grade,boolean taken) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.complementaryValues = new ComplementaryValues();
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

	public void setGrade(double grade) {
		this.grade = grade;
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
	}

	public String getSubjectValuesPublicId() {
		return subjectValuesPublicId;
	}

	public void setSubjectValuesPublicId(String subjectValuesPublicId) {
		this.subjectValuesPublicId = subjectValuesPublicId;
	}

	public ComplementaryValues getComplementaryValues() {
		return complementaryValues;
	}

	public void setComplementaryValues(ComplementaryValues complementaryValues) {
		this.complementaryValues = complementaryValues;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
