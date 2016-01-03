package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.somosun.plan.server.control.SingleScoreServer;
import com.somosun.plan.shared.control.SemesterValue;

public abstract class SingleScoreAbstract implements Serializable{

	@Id private Long id = null;
	private Double average = null;
	private int amount = 0;
	
	public abstract SemesterValue getSemesterValue();

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
	
	/**
	 * This method will compare just the direct values [including id] (i.e. will compare the ref value and not the values of the entity referred)
	 * 
	 * @param sS
	 * @return
	 */
	public boolean compare(SingleScoreAbstract sS){
		boolean toReturn = false;
		
		if(this.getId() == sS.getId() && this.getAmount() == sS.getAmount() && this.getAverage() == sS.getAverage() && this.getSemesterValue() == this.getSemesterValue()){
			toReturn = true;
		}
		
		return toReturn;
	}
	
}
