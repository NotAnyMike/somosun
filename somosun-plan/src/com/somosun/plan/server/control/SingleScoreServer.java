package com.somosun.plan.server.control;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.incomplete.SingleScoreIncomplete;

@Entity
public class SingleScoreServer extends SingleScoreIncomplete {

	@Index @Load private Ref<SemesterValue> semesterValue = null;
	
	public SingleScoreServer(){}

	public Ref<SemesterValue> getSemesterValue() {
		return semesterValue;
	}

	public void setSemesterValue(Ref<SemesterValue> semesterValue) {
		this.semesterValue = semesterValue;
	}
	
}
