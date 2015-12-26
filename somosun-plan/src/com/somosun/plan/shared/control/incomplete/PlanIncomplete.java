package com.somosun.plan.shared.control.incomplete;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

public class PlanIncomplete implements Serializable {

	private static final long serialVersionUID = 1L;
	@Index @Id Long id=null;
	private String name = null;
    private double gpa = 0;
    @Index private boolean isDefault = false;

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
