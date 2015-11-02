package com.somosun.plan.shared;

import java.io.Serializable;
import java.util.List;

import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.SubjectGroup;

public class CompletePlanInfo implements Serializable{
	
	private Career career = null;
	private Plan planDefautl = null;
	private List<ComplementaryValue> mandatoryComplementaryValues = null;
	private List<ComplementaryValue> dummyComplementaryValues = null;
	private List<SubjectGroup> subjectGroups = null;
	
	public CompletePlanInfo(){
	}

	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public List<SubjectGroup> getSubjectGroups() {
		return subjectGroups;
	}

	public void setSubjectGroups(List<SubjectGroup> subjectGroups) {
		this.subjectGroups = subjectGroups;
	}

	public Plan getPlanDefautl() {
		return planDefautl;
	}

	public void setPlanDefautl(Plan planDefautl) {
		this.planDefautl = planDefautl;
	}

	public List<ComplementaryValue> getMandatoryComplementaryValues() {
		return mandatoryComplementaryValues;
	}

	public void setMandatoryComplementaryValues(
			List<ComplementaryValue> mandatoryComplementaryValue) {
		this.mandatoryComplementaryValues = mandatoryComplementaryValue;
	}

	public List<ComplementaryValue> getDummyComplementaryValues() {
		return dummyComplementaryValues;
	}

	public void setDummyComplementaryValues(List<ComplementaryValue> dummyComplementaryValues) {
		this.dummyComplementaryValues = dummyComplementaryValues;
	}

}
