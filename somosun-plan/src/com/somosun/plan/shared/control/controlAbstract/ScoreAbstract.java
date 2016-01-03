package com.somosun.plan.shared.control.controlAbstract;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.somosun.plan.server.control.ScoreServer;
import com.somosun.plan.server.control.SingleScoreServer;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.Teacher;

public abstract class ScoreAbstract implements Serializable {

	@Id private Long id = null;
	@Index private double totalAverage = 0.0;
	private int totalAmount = 0;
	
	public abstract Teacher getTeacher();
	public abstract Subject getSubject();
	public abstract List<SingleScore> getScores();
	
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
	
	public boolean compare(ScoreAbstract s){
		boolean toReturn = false;
		
		if(s != null){
			if(this.getId() == s.getId() && this.getTotalAmount() == s.getTotalAmount() && this.getTotalAverage() == s.getTotalAverage() && 
					this.getTeacher().getIdSun().equals(s.getTeacher().getIdSun()) && this.getSubject().getId().equals(s.getSubject().getId())){
				List<SingleScore> list = this.getScores();
				boolean isEqual = true;
				if(list.size() == s.getScores().size()){
					for(int x = 0; x < list.size(); x++){
						if(list.get(x).getId().equals(s.getScores().get(x).getId()) == false){
							isEqual = false;
							break;
						}
					}
					if(isEqual == true) toReturn = true;
				}
			}
		}
		
		return toReturn;
	}
	
}
