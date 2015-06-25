package com.uibinder.index.server;

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

}
