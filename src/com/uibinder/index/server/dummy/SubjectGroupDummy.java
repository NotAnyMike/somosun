package com.uibinder.index.server.dummy;

import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class SubjectGroupDummy {
	
	private String name = null;
	private int obligatoryCredits = 0;
	private int optativeCredits = 0;
	private Boolean fundamental = null;
	private boolean error = false;
	private Elements tds = null;
	private Element table = null;
	
	public SubjectGroupDummy(String name, int obligatoryCredits, int optativeCredits, boolean fundamental, Elements tds) {
		super();
		this.setName(name);
		this.setObligatoryCredits(obligatoryCredits);
		this.setOptativeCredits(optativeCredits);
		this.setTds(tds);
		this.fundamental = fundamental;
	}
	
	public SubjectGroupDummy(String name, int obligatoryCredits, int optativeCredits, boolean fundamental, Element table) {
		super();
		this.setName(name);
		this.setObligatoryCredits(obligatoryCredits);
		this.setOptativeCredits(optativeCredits);
		this.setTable(table);
		this.fundamental = fundamental;
	}
	
	public SubjectGroupDummy(String name, int obligatoryCredits, int optativeCredits, boolean fundamental, Elements tds, Element table) {
		super();
		this.setName(name);
		this.setObligatoryCredits(obligatoryCredits);
		this.setOptativeCredits(optativeCredits);
		this.setTds(tds);
		this.setTable(table);
		this.fundamental = fundamental;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public Boolean isFundamental() {
		return fundamental;
	}

	public void setFundamental(Boolean fundamental) {
		this.fundamental = fundamental;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Element getTable() {
		return table;
	}

	public void setTable(Element table) {
		this.table = table;
	}

	public int getOptativeCredits() {
		return optativeCredits;
	}

	public void setOptativeCredits(int optativeCredits) {
		this.optativeCredits = optativeCredits;
	}

	public int getObligatoryCredits() {
		return obligatoryCredits;
	}

	public void setObligatoryCredits(int obligatoryCredits) {
		this.obligatoryCredits = obligatoryCredits;
	}

	public Elements getTds() {
		return tds;
	}

	public void setTds(Elements tds) {
		this.tds = tds;
	}

}
