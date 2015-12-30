package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.SubjectGroup;

public abstract class SubjectGroupAbstract<T extends SubjectGroupAbstract> implements Serializable {

	@Id private Long id = null;
	@Index private String name = null;
	@Index private Boolean fundamental = null;
	private int obligatoryCredits = 0;
	private int optativeCredits = 0;
	@Index private boolean error = false;
	
	abstract public boolean equals(T g);
	abstract public void setCareer(Career career);
	abstract public Career getCareer();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isFundamental() {
		return fundamental;
	}

	public void setFundamental(Boolean fundamental) {
		this.fundamental = fundamental;
	}

	public int getObligatoryCredits() {
		return obligatoryCredits;
	}

	public void setObligatoryCredits(int obligatoryCredits) {
		this.obligatoryCredits = obligatoryCredits;
	}

	public int getOptativeCredits() {
		return optativeCredits;
	}

	public void setOptativeCredits(int optativeCredits) {
		this.optativeCredits = optativeCredits;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean ok) {
		this.error = ok;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
