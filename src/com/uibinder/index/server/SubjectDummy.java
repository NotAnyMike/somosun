package com.uibinder.index.server;

import java.util.List;

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
	private String text = null;
	private boolean error = false;
	
	public SubjectDummy(String code, String name, int credits,	SubjectGroupDummy subjectGroupDummy, List<SubjectDummy> preRequisites, List<SubjectDummy> coRequisites, Boolean mandatory, String text) {
		this.code = code;
		this.name = name;
		this.credits = credits;
		this.subjectGroupDummy = subjectGroupDummy;
		this.preRequisites = preRequisites;
		this.coRequisites = coRequisites;
		this.mandatory = mandatory;
		this.text = text;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

}
