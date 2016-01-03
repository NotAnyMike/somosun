package com.somosun.plan.server.control;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.controlAbstract.SingleScoreAbstract;

@Entity
public class SingleScoreServer extends SingleScoreAbstract {

	@Index @Load private Ref<SemesterValue> semesterValue = null;
	
	public SingleScoreServer(){}

	public Ref<SemesterValue> getSemesterValueRef() {
		return semesterValue;
	}
	
	public SemesterValue getSemesterValue(){
		SemesterValue sV = null;
		if(semesterValue != null) sV = semesterValue.get();
		return sV;
	}

	public void setSemesterValue(Ref<SemesterValue> semesterValue) {
		this.semesterValue = semesterValue;
	}

	public SingleScore getClientInstance() {
		SingleScore ss = new SingleScore();
		
		ss.setId(this.getId());
		ss.setAmount(getAmount());
		ss.setAverage(getAverage());
		ss.setSemesterValue(this.getSemesterValue());
		
		
		return ss;
	}
	
}
