package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.somosun.plan.shared.control.SemesterValue;

public abstract class SingleScoreAbstract implements Serializable{

	@Id private Long id = null;
	private Double average = null;
	private int amount = 0;

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
