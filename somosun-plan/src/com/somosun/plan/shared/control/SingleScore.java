package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.somosun.plan.shared.control.incomplete.SingleScoreIncomplete;

public class SingleScore extends SingleScoreIncomplete implements Serializable{
	
	private SemesterValue semesterValue = null;
	
	public SingleScore(){}

	public SemesterValue getSemesterValue() {
		return semesterValue;
	}

	public void setSemesterValue(SemesterValue semesterValue) {
		this.semesterValue = semesterValue;
	}

	
	
}
