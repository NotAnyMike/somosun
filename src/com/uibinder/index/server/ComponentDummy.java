package com.uibinder.index.server;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class ComponentDummy {

	private Boolean fundamental = null;
	private int obligatoryCredits = 0;
	private int optativeCredits = 0;
	private List<SubjectGroupDummy> elements = null;
	private Element table = null;
	private boolean error = false;
	
	public ComponentDummy(Boolean fundamental, int obligatoryCredits,	int optativeCredits, Element table) {
		this.setFundamental(fundamental);
		this.setObligatoryCredits(obligatoryCredits);
		this.setOptativeCredits(optativeCredits);
		this.table = table;
		elements = new ArrayList<SubjectGroupDummy>();
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public void addGroupSubject(SubjectGroupDummy sGD) {
		elements.add(sGD);
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

	public Boolean isFundamental() {
		return fundamental;
	}

	public void setFundamental(Boolean fundamental) {
		this.fundamental = fundamental;
	}
	
}
