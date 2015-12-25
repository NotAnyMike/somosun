package com.somosun.plan.shared.control.incomplete;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

public class ScoreIncomplete {

	@Id private Long id = null;
	@Index private double totalAverage = 0.0;
	private int totalAmount = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getTotalAverage() {
		return totalAverage;
	}

	public void setTotalAverage(double totalAverage) {
		this.totalAverage = totalAverage;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
