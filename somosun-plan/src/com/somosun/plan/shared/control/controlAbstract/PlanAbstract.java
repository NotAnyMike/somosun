package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Student;

public abstract class PlanAbstract implements Serializable {

	private static final long serialVersionUID = 1L;
	@Index @Id Long id=null;
	private String name = null;
    private double gpa = 0;
    @Index private boolean isDefault = false;
    
    public abstract Career getCareer();
    public abstract void setCareer(Career c);
    public abstract List<SemesterAbstract> getSemesters();
    public abstract void setSemesters(List<SemesterAbstract> semesters);
    public abstract void setUser(Student s);
    public abstract Student getUser();

	public Long getId() {
		return id;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double papa) {
		this.gpa = papa;
	}

	public void setId(Long id) {
		this.id = id;
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
	
}
