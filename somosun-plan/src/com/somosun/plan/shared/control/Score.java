package com.somosun.plan.shared.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.somosun.plan.shared.control.controlAbstract.ScoreAbstract;

@Entity
public class Score extends ScoreAbstract implements Serializable {

	private Teacher teacher = null;
	private Subject subject = null;
	private List<SingleScore> scores = null;
	
	public Score(){}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public List<SingleScore> getScores() {
		return scores;
	}

	public void setScores(List<SingleScore> scores) {
		this.scores = scores;
	}
	
	public void addSingleScore(SingleScore singleScore){
		if(scores == null) scores = new ArrayList<SingleScore>();
		scores.add(singleScore);
	}
	
	public SingleScore getSemester(Double semesterNumber) {
		SingleScore toReturn = null;
		
		if(scores != null && !scores.isEmpty()){			
			for(SingleScore singleScore : scores){
				if(singleScore.getSemesterValue().toStringDouble().equals(semesterNumber.toString())){
					toReturn = singleScore;
					break;
				}
			}
		}
		
		return toReturn;
	}

}
