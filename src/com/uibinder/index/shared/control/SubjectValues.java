package com.uibinder.index.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
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
	@Index Group group;
    double grade;
    int timesSeen;
    String typology;
    
    public SubjectValues(){
    }

    public SubjectValues(Group group, double grade,int timesSeen,String type) {
        this.group = group;
        this.grade = grade;
        this.timesSeen=timesSeen;
        this.typology=type;
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

	public int getTimesSeen() {
		return timesSeen;
	}

	public void setTimesSeen(int timesSeen) {
		this.timesSeen = timesSeen;
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
}
