package com.somosun.plan.shared.control;

import java.io.Serializable;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;

@Entity
public class SingleScore implements Serializable{

	@Id private Long id = null;
//	@Load @Index private Ref<SemesterValue> semesterValueRef = null;
	@Index private SemesterValue semesterValue = null;
	private Double average = null;
	private int amount = 0;
	
	public SingleScore(){}
	
//	@OnLoad
//	private void onLoad(){
//		semesterValue = semesterValueRef.get();
//	}

	public SemesterValue getSemesterValue() {
		return semesterValue;
	}

	public void setSemesterValue(SemesterValue semesterValue) {
		this.semesterValue = semesterValue;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @param amountToAdd
	 * @param averageToAdd
	 */
	public void sumToAmountAndAverage(int amountToAdd, double averageAgregatedToAdd) {
		setAverage((getAverage()*getAmount() + averageAgregatedToAdd)/(getAmount() + amountToAdd));
		setAmount(getAmount() + amountToAdd);
		
	}
	
}
