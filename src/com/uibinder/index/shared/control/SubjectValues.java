package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author Cesar A. Villamizar C.
 */
@Entity
public class SubjectValues implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id private Long id = null;
	@Index private Group group = null;
	@Ignore private String subjectValuesPublicId = null;
    private double grade = 0;
    private boolean taken = false;
    private String typology = null;
    private boolean obligatoriness = false;
    
    public SubjectValues(){
    }

    public SubjectValues(Group group, double grade,boolean taken,String typology) {
        this.group = group;
        this.grade = grade;
        this.taken = taken;
        this.typology=typology;
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

	public String getTypology() {
		return typology;
	}

	public void setTypology(String typology) {
		this.typology = typology;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public boolean isObligatoriness() {
		return obligatoriness;
	}

	public void setObligatoriness(boolean obligatoriness) {
		this.obligatoriness = obligatoriness;
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
}
