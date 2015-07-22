package com.uibinder.shared;

import java.io.Serializable;

public class PlanValuesResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private String value = null;
	
	public PlanValuesResult(){
	}
	
	public PlanValuesResult(String name, String value){
		this.setName(name);
		this.setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
