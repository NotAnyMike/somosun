package com.uibinder.server.dummy;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TODO: When there are optional requisites, i.e. metodolgía de la investigación
 * 
 * @author Mike
 *
 */
public class SubjectDummy {
	
	private String code = null;
	private String name = null;
	private int credits = 0;
	private SubjectGroupDummy subjectGroupDummy = null;
	private List<SubjectDummy> preRequisites = null;
	private List<SubjectDummy> coRequisites = null;
	private Boolean mandatory = null;
	private Element[] elements = null;
	private boolean error = false;
	
	/************** to use only in string from academic history ********************/
	private Double grade = null;
	private String typology = null;
	private Integer group = null;
	private boolean approved = false; // true is grade takes the valus ap or np
	
	public SubjectDummy(){
	}
	
	public SubjectDummy(String code, String name, int credits,	SubjectGroupDummy subjectGroupDummy, List<SubjectDummy> preRequisites, List<SubjectDummy> coRequisites, Boolean mandatory, Element[] elements) {
		this.code = code;
		this.name = name;
		this.credits = credits;
		this.subjectGroupDummy = subjectGroupDummy;
		this.preRequisites = preRequisites;
		this.coRequisites = coRequisites;
		this.mandatory = mandatory;
		this.elements = elements;
	}
	
	public SubjectDummy(String name){
		this.name = name;
	}
	
	public SubjectDummy(String name, String code){
		this.name = name;
		this.code = code;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public SubjectGroupDummy getSubjectGroupDummy() {
		return subjectGroupDummy;
	}

	public void setSubjectGroupDummy(SubjectGroupDummy subjectGroupDummy) {
		this.subjectGroupDummy = subjectGroupDummy;
	}

	public List<SubjectDummy> getPreRequisites() {
		return preRequisites;
	}

	public void setPreRequisites(List<SubjectDummy> preRequisites) {
		this.preRequisites = preRequisites;
	}

	public List<SubjectDummy> getCoRequisites() {
		return coRequisites;
	}

	public void setCoRequisites(List<SubjectDummy> coRequisites) {
		this.coRequisites = coRequisites;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Element[] getElements() {
		return elements;
	}

	public void setElements(Element[] elements) {
		this.elements = elements;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public String getTypology() {
		return typology;
	}

	public void setTypology(String typology) {
		this.typology = typology;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

}
