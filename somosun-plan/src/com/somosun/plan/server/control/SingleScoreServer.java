package com.somosun.plan.server.control;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.controlAbstract.SingleScoreAbstract;

@Entity
public class SingleScoreServer extends SingleScoreAbstract {

	@Index @Load private Ref<SemesterValue> semesterValue = null;
	
	public SingleScoreServer(){}

	public Ref<SemesterValue> getSemesterValue() {
		return semesterValue;
	}

	public void setSemesterValue(Ref<SemesterValue> semesterValue) {
		this.semesterValue = semesterValue;
	}
	
	/**
	 * This method will compare just the direct values [including id] (i.e. will compare the ref value and not the values of the entity referred)
	 * 
	 * @param sS
	 * @return
	 */
	public boolean compare(SingleScoreServer sS){
		boolean toReturn = false;
		
		if(this.getId() == sS.getId() && this.getAmount() == sS.getAmount() && this.getAverage() == sS.getAverage() && this.getSemesterValue() == this.getSemesterValue()){
			toReturn = true;
		}
		
		return toReturn;
	}
	
}
