package com.somosun.plan.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will carry the id of the semesters, 
 * subjectValues or complementaryValue in order to avoid null id just before saving any plan
 * @author MW
 *
 */
public class IdContainer implements Serializable {

	private List<Long> semestersIds = new ArrayList<Long>();
	private List<Long> subjectValuesIds = new ArrayList<Long>();
	private List<Long> complementaryValuesIds = new ArrayList<Long>();
	
	public IdContainer(){}

	public List<Long> getSemestersIds() {
		return semestersIds;
	}

	public void setSemestersIds(List<Long> semestersIds) {
		this.semestersIds = semestersIds;
	}

	public List<Long> getSubjectValuesIds() {
		return subjectValuesIds;
	}

	public void setSubjectValuesIds(List<Long> subjectValuesIds) {
		this.subjectValuesIds = subjectValuesIds;
	}

	public List<Long> getComplementaryValuesIds() {
		return complementaryValuesIds;
	}

	public void setComplementaryValuesIds(List<Long> complementaryValuesIds) {
		this.complementaryValuesIds = complementaryValuesIds;
	}
	
	public void addSemesterId(Long id){
		if(id != null)semestersIds.add(id);
	}
	
	public void addSubjectValueId(Long id){
		if(id != null) subjectValuesIds.add(id);
	}
	
	public void addComplementaryValueIds(Long id){
		if(id != null) complementaryValuesIds.add(id);
	}
	
}
